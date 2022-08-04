package com.castcle.android.data.user

import androidx.room.withTransaction
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.user.entity.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
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

    override suspend fun commentCast(contentId: String, message: String) {
        val body = CommentRequest(contentId = contentId, message = message)
        val response = apiCall { api.commentCast(body = body) }
        val comment = CommentEntity.map(listOf(), response?.payload)
        val sessionId = database.content().getExistSessionIdByCastId(
            castId = contentId,
            type = ContentType.Content,
        )
        val newContent = sessionId.map {
            ContentEntity(
                commentId = comment.id,
                commentUserId = comment.authorId,
                createdAt = comment.createdAt,
                sessionId = it,
                type = ContentType.Comment,
            )
        }
        database.withTransaction {
            database.cast().increaseCommentCount(contentId)
            database.comment().insert(listOf(comment))
            database.content().insert(newContent)
            database.content().updateLastComment(sessionId)
        }
    }

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

    override suspend fun likeComment(comment: CommentEntity) {
        val updateItem = comment.copy(
            likeCount = comment.likeCount.plus(1),
            liked = true,
        )
        database.comment().update(item = updateItem)
        apiCall { api.likeComment(LikeCommentRequest(commentId = comment.id)) }
    }

    override suspend fun replyComment(commentId: String, message: String) {
        val body = CommentRequest(message = message)
        val response = apiCall { api.replyComment(commentId = commentId, body = body) }
        val comment = CommentEntity.map(listOf(), response?.payload)
        val sessionId = database.content().getExistSessionIdByCommentId(
            commentId = commentId,
            type = ContentType.Comment,
        )
        val newContent = sessionId.map {
            ContentEntity(
                commentId = comment.id,
                commentUserId = comment.authorId,
                createdAt = it.createdAt,
                replyAt = comment.createdAt,
                sessionId = it.sessionId,
                type = ContentType.Reply,
            )
        }
        database.withTransaction {
            database.comment().insert(listOf(comment))
            database.content().insert(newContent)
            database.content().updateLastComment(sessionId.map { it.sessionId })
        }
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

    override suspend fun unlikeComment(comment: CommentEntity) {
        val updateItem = comment.copy(
            likeCount = comment.likeCount.minus(1),
            liked = false,
        )
        database.comment().update(item = updateItem)
        apiCall { api.unlikeComment(commentId = comment.id) }
    }

}