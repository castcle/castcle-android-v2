package com.castcle.android.data.user.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.user.mapper.FollowingFollowersResponseMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.user.entity.FollowingFollowersWithResultEntity

@ExperimentalPagingApi
class FollowingFollowersRemoteMediator(
    private val api: UserApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val isFollowing: Boolean,
    private val mapper: FollowingFollowersResponseMapper,
    private val sessionId: Long,
    private val userId: String,
) : RemoteMediator<Int, FollowingFollowersWithResultEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FollowingFollowersWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.followingFollowers().delete(sessionId) }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = if (isFollowing) {
                api.getFollowing(
                    id = userId,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
            } else {
                api.getFollowers(
                    id = userId,
                    maxResults = state.config.pageSize,
                    untilId = loadKey,
                )
            }

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(ownerUserId, response.body(), sessionId)
            } else {
                return MediatorResult.Error(ErrorMapper().map(response.errorBody()))
            }

            with(glidePreloader) {
                loadUser(items = items.user)
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.FollowingFollowers,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.followingFollowers().insert(items.followingFollowers)
                database.loadKey().insert(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(ErrorMapper().map(exception))
        }
    }

}