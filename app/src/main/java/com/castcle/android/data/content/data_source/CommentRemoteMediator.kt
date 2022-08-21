package com.castcle.android.data.content.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.custom_view.load_state.item_empty_state_content.EmptyStateContentViewEntity
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.*
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.content.mapper.CommentResponseMapper
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.entity.ContentWithResultEntity
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType

@ExperimentalPagingApi
class CommentRemoteMediator(
    private val api: ContentApi,
    private val content: ContentEntity,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val mapper: CommentResponseMapper,
    private val sessionId: Long,
) : RemoteMediator<Int, ContentWithResultEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContentWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.content().delete(sessionId) }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getComment(
                contentId = content.originalCastId.orEmpty(),
                maxResults = state.config.pageSize,
                untilId = loadKey,
            )

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(content, loadType, ownerUserId, response.body(), sessionId)
            } else {
                val error = when (val exception = ErrorMapper().map(response.errorBody())) {
                    is CastcleException.ContentNotFoundException -> RetryException(
                        error = exception,
                        errorItems = EmptyStateContentViewEntity.create(1),
                    )
                    else -> exception
                }
                return MediatorResult.Error(error)
            }

            glidePreloader.loadUser(items = items.user)

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.Content,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.comment().insert(items.comment)
                database.content().insert(items.content)
                database.content().updateLastComment(sessionId)
                database.loadKey().insert(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(ErrorMapper().map(exception))
        }
    }

}