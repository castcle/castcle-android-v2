package com.castcle.android.data.user.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.error.ApiException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.user.mapper.ProfileResponseMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.user.entity.ProfileWithResultEntity
import com.castcle.android.domain.user.entity.UserEntity

@ExperimentalPagingApi
class ProfileRemoteMediator(
    private val api: UserApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val mapper: ProfileResponseMapper,
    private val sessionId: Long,
    private val user: UserEntity,
) : RemoteMediator<Int, ProfileWithResultEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProfileWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.profile().delete(sessionId) }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getUserCast(
                id = user.id,
                maxResults = state.config.pageSize,
                untilId = loadKey,
            )

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(user, sessionId, loadType, ownerUserId, response.body())
            } else {
                return MediatorResult.Error(ApiException.map(response.errorBody()))
            }

            with(glidePreloader) {
                loadCast(items = items.cast)
                loadUser(items = items.user)
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.Profile,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.cast().insert(items.cast)
                database.profile().insert(items.profile)
                database.loadKey().insert(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}