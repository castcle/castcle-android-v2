package com.castcle.android.data.search.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.constants.PARAMETER_CONTENT_TYPE_PHOTO
import com.castcle.android.core.error.ApiException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.search.mapper.SearchResponseMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.search.entity.SearchWithResultEntity
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.user.type.UserType

@ExperimentalPagingApi
class SearchRemoteMediator(
    private val api: SearchApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val keywordSessionId: Long,
    private val mapper: SearchResponseMapper,
    private val sessionId: Long,
    private val type: SearchType,
) : RemoteMediator<Int, SearchWithResultEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.search().delete(sessionId) }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val keyword = database.withTransaction {
                database.searchKeyword().get(keywordSessionId).firstOrNull()?.keyword.orEmpty()
            }

            val response = when (type) {
                SearchType.Trend -> api.searchTrend(
                    keyword = keyword,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
                SearchType.Lastest -> api.searchLatest(
                    keyword = keyword,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
                SearchType.Photo -> api.searchTrend(
                    contentType = PARAMETER_CONTENT_TYPE_PHOTO,
                    keyword = keyword,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
                SearchType.People -> api.searchUser(
                    keyword = keyword,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
            }

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(response.body(), ownerUserId, sessionId)
            } else {
                return MediatorResult.Error(ApiException.map(response.errorBody()))
            }

            with(glidePreloader) {
                loadCast(items = items.cast)
                loadUser(items = items.user)
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.Search,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.cast().insert(items.cast)
                database.search().insert(items.search)
                database.loadKey().insert(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = nextLoadKey.loadKey == null)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}