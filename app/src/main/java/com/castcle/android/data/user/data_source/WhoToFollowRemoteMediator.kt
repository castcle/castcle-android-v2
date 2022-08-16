package com.castcle.android.data.user.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ApiException
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.user.entity.*

@ExperimentalPagingApi
class WhoToFollowRemoteMediator(
    private val api: UserApi,
    private val database: CastcleDatabase,
) : RemoteMediator<Int, WhoToFollowWithResultEntity>() {

    private val sessionId = System.currentTimeMillis()

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WhoToFollowWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.whoToFollow().delete() }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getWhoToFollow(
                maxResults = state.config.pageSize,
                untilId = loadKey,
            )

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                UserEntity.map(ownerUserId, response.body()?.payload) to
                    WhoToFollowEntity.map(response.body()?.payload)
            } else {
                return MediatorResult.Error(ApiException.map(response.errorBody()))
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.WhoToFollow,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.loadKey().replace(nextLoadKey)
                database.user().upsert(items.first)
                database.whoToFollow().insert(items.second)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}