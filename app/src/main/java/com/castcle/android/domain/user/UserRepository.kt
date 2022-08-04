package com.castcle.android.domain.user

import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.UserEntity

interface UserRepository {
    suspend fun commentCast(contentId: String, message: String)
    suspend fun followUser(targetUser: UserEntity)
    suspend fun fetchUserPage(): List<UserEntity>
    suspend fun fetchUserProfile(): UserEntity
    suspend fun getUser(id: String): UserEntity
    suspend fun likeCasts(content: CastEntity)
    suspend fun likeComment(comment: CommentEntity)
    suspend fun replyComment(commentId: String, message: String)
    suspend fun unfollowUser(targetUser: UserEntity)
    suspend fun unlikeCasts(content: CastEntity)
    suspend fun unlikeComment(comment: CommentEntity)
}