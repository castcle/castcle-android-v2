package com.castcle.android.data.user

import com.castcle.android.core.api.UserApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.user.entity.FollowUserRequest
import com.castcle.android.data.user.entity.LikeCastsRequest
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(
    private val api: UserApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
) : UserRepository {

    override suspend fun followUser(targetUser: UserEntity) {
        database.user().update(item = targetUser.copy(followed = true))
        apiCall { api.followUser(FollowUserRequest(targetCastcleId = targetUser.castcleId)) }
    }

    override suspend fun fetchUserPage(): List<UserEntity> {
        val response = apiCall { api.getUserPage() }
        val syncSocial = SyncSocialEntity.map(response?.payload)
        val user = UserEntity.mapOwner(response?.payload)
        glidePreloader.loadUser(user)
        database.syncSocial().insert(syncSocial)
        database.user().upsert(user)
        return user
    }

    override suspend fun fetchUserProfile(): UserEntity {
        val response = apiCall { api.getUser(id = "me") }
        val syncSocial = SyncSocialEntity.map(response)
        val user = UserEntity.mapOwner(response)
        glidePreloader.loadUser(user)
        database.syncSocial().insert(syncSocial)
        database.user().upsert(user)
        return user
    }

    override suspend fun getUser(id: String): UserEntity {
        val ownerUserId = database.user().get().map { it.id }
        val user = UserEntity.map(ownerUserId, apiCall { api.getUser(id = id) })
        database.user().upsert(user)
        return user
    }

    override suspend fun likeCasts(content: CastEntity) {
        val updateItem = content.copy(
            likeCount = content.likeCount.plus(1),
            liked = true,
        )
        database.cast().update(item = updateItem)
        apiCall { api.likeCasts(LikeCastsRequest(contentId = content.id)) }
    }

    override suspend fun unfollowUser(targetUser: UserEntity) {
        database.user().update(item = targetUser.copy(followed = false))
        apiCall { api.unfollowUser(targetCastcleId = targetUser.castcleId) }
    }

    override suspend fun unlikeCasts(content: CastEntity) {
        val updateItem = content.copy(
            likeCount = content.likeCount.minus(1),
            liked = false,
        )
        database.cast().update(item = updateItem)
        apiCall { api.unlikeCasts(contentId = content.id) }
    }

}