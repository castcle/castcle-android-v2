package com.castcle.android.data.feed.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.FeedApi
import com.castcle.android.core.constants.PARAMETER_CIRCLE_SLUG_FOR_YOU
import com.castcle.android.core.constants.PARAMETER_FEATURE_SLUG_FEED
import com.castcle.android.core.error.ApiException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.feed.mapper.FeedResponseMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.feed.entity.FeedWithResultEntity
import com.castcle.android.domain.user.entity.UserEntity

@ExperimentalPagingApi
class FeedRemoteMediator(
    private val api: FeedApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val isGuest: Boolean,
    private val mapper: FeedResponseMapper,
    private val user: UserEntity?,
) : RemoteMediator<Int, FeedWithResultEntity>() {

    private val sessionId = System.currentTimeMillis()

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.feed().delete()
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = if (isGuest) {
                api.getFeedGuest(
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
            } else {
                api.getFeedMember(
                    circleSlug = PARAMETER_CIRCLE_SLUG_FOR_YOU,
                    featureSlug = PARAMETER_FEATURE_SLUG_FEED,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(user, loadType, response.body(), isGuest)
            } else {
                return MediatorResult.Error(ApiException.map(response.errorBody()))
            }

            with(glidePreloader) {
                loadCast(items = items.cast)
                loadUser(items = items.user)
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.Feed,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.cast().insert(items.cast)
                database.feed().insert(items.feed)
                database.loadKey().replace(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}