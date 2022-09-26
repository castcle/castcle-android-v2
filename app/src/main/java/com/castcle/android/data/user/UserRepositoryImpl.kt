/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.data.user

import android.content.Context
import androidx.room.withTransaction
import com.castcle.android.core.api.AuthenticationApi
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.core.extensions.*
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.authentication.entity.GetGuestAccessTokenRequest
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.data.user.entity.*
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.ProfileType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.sign_up.update_profile.entity.UploadImageRequest
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(
    private val api: UserApi,
    private val authenticationApi: AuthenticationApi,
    private val context: Context,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
) : UserRepository {

    override suspend fun commentCast(contentId: String, message: String) {
        val body = CommentRequest(contentId = contentId, message = message)
        val response = apiCall { api.commentCast(body = body) }
        val ownerUserId = database.user().get().map { it.id }
        val comment = CommentEntity.map(ownerUserId, response?.payload)
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

    override suspend fun createQuoteCast(body: CreateQuoteCastRequest, userId: String) {
        val response = apiCall { api.createQuoteCast(body = body, id = userId) }
        val ownerUserId = database.user().get().map { it.id }
        val content = CastEntity.map(ownerUserId, response?.payload)
        val quoteCast = CastEntity.map(ownerUserId, response?.includes?.casts).find {
            it.id == response?.payload?.referencedCasts?.id
        }
        val sessionId = database.profile().getExistSessionIdByUserId(
            type = ProfileType.Profile,
            userId = userId,
        )
        val newProfile = sessionId.map {
            ProfileEntity(
                createdAt = content.createdAt.toMilliSecond() ?: 0L,
                originalCastId = content.id,
                originalUserId = content.authorId,
                referenceCastId = quoteCast?.id,
                referenceUserId = quoteCast?.authorId,
                sessionId = it,
                type = ProfileType.Content,
            )
        }
        database.withTransaction {
            database.cast().increaseQuoteCastCount(body.contentId.orEmpty())
            database.cast().insert(content)
            database.profile().insert(newProfile)
            database.user().increaseCastCount(userId)
        }
    }

    override suspend fun deleteAccount(body: DeleteAccountRequest) {
        apiCall { api.deleteAccount(body = body) }
        database.withTransaction {
            fetchGuestAccessToken()
            database.cast().delete()
            database.linkSocial().delete()
            database.notificationBadges().delete()
            database.profile().delete()
            database.recursiveRefreshToken().delete()
            database.syncSocial().delete()
            database.user().delete()
        }
    }

    override suspend fun deleteComment(commentId: String) {
        apiCall { api.deleteComment(commentId = commentId) }
        database.withTransaction {
            val comment = database.content().get(commentId = commentId)
            val content = database.content()
                .get(sessionId = comment?.sessionId ?: 0L, type = ContentType.Content)
                .firstOrNull()
            database.cast().decreaseCommentCount(content?.originalCastId.orEmpty())
            database.content().deleteByCreatedAt(createdAt = comment?.createdAt ?: 0L)
        }
    }

    override suspend fun deleteReplyComment(replyCommentId: String) {
        val replyComment = database.content().get(commentId = replyCommentId)
        val comment = database.content()
            .getByCreatedAt(createdAt = replyComment?.createdAt ?: 0L, type = ContentType.Comment)
            .firstOrNull()
        apiCall {
            api.deleteReplyComment(
                commentId = comment?.commentId.orEmpty(),
                replyCommentId = replyCommentId,
            )
        }
        database.content().deleteByCommentId(commentId = replyComment?.commentId.orEmpty())
    }

    private suspend fun fetchGuestAccessToken() {
        val body = GetGuestAccessTokenRequest(context.getDeviceUniqueId())
        val response = apiCall { authenticationApi.getGuestAccessToken(body = body) }
        database.accessToken().insert(AccessTokenEntity.map(response))
    }

    override suspend fun followUser(targetUser: UserEntity) {
        database.withTransaction {
            val currentUserId = database.user().get(UserType.People).firstOrNull()?.id.orEmpty()
            database.user().update(item = targetUser.copy(followed = true))
            database.user().increaseFollowers(userId = targetUser.id)
            database.user().increaseFollowing(userId = currentUserId)
        }
        apiCall { api.followUser(FollowUserRequest(targetCastcleId = targetUser.castcleId)) }
    }

    override suspend fun fetchUserPage(): List<UserEntity> {
        val response = apiCall { api.getUserPage() }
        val syncSocial = SyncSocialEntity.map(response?.payload)
        val user = UserEntity.mapOwner(response?.payload)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.syncSocial().delete(user.map { it.id })
            database.syncSocial().insert(syncSocial)
            database.user().upsert(user)
        }
        return user
    }

    override suspend fun fetchUserProfile(): UserEntity {
        val response = apiCall { api.getUser(id = "me") }
        val linkSocial = LinkSocialEntity.map(response)
        val syncSocial = SyncSocialEntity.map(response)
        val user = UserEntity.mapOwner(response)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.linkSocial().delete()
            database.linkSocial().insert(linkSocial)
            database.syncSocial().delete(listOf(user.id))
            database.syncSocial().insert(syncSocial)
            database.user().upsert(user)
        }
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

    override suspend fun recastContent(contentId: String, userId: String) {
        val body = RecastContentRequest(contentId = contentId)
        val response = apiCall { api.recastContent(body = body, id = userId) }
        val ownerUserId = database.user().get().map { it.id }
        val content = CastEntity.map(ownerUserId, response?.payload)
        val referenceCast = CastEntity.map(ownerUserId, response?.includes?.casts).find {
            it.id == response?.payload?.referencedCasts?.id
        }
        val sessionId = database.profile().getExistSessionIdByUserId(
            type = ProfileType.Profile,
            userId = userId,
        )
        val newProfile = sessionId.map {
            ProfileEntity(
                createdAt = content.createdAt.toMilliSecond() ?: 0L,
                originalCastId = content.id,
                originalUserId = content.authorId,
                referenceCastId = referenceCast?.id,
                referenceUserId = referenceCast?.authorId,
                sessionId = it,
                type = ProfileType.Content,
            )
        }
        database.withTransaction {
            database.cast().increaseRecastCount(contentId)
            database.cast().insert(content)
            database.cast().updateRecasted(contentId, true)
            database.profile().insert(newProfile)
            database.user().increaseCastCount(userId)
        }
    }

    override suspend fun replyComment(commentId: String, message: String) {
        val body = CommentRequest(message = message)
        val response = apiCall { api.replyComment(commentId = commentId, body = body) }
        val ownerUserId = database.user().get().map { it.id }
        val comment = CommentEntity.map(ownerUserId, response?.payload)
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

    override suspend fun reportContent(body: ReportRequest) {
        apiCall { api.reportContent(body = body) }
        database.cast().updateReported(castId = body.targetContentId.orEmpty(), reported = true)
    }

    override suspend fun reportUser(body: ReportRequest) {
        apiCall { api.reportUser(body = body) }
    }

    override suspend fun unfollowUser(targetUser: UserEntity) {
        database.withTransaction {
            val currentUserId = database.user().get(UserType.People).firstOrNull()?.id.orEmpty()
            database.user().update(item = targetUser.copy(followed = false))
            database.user().decreaseFollowers(userId = targetUser.id)
            database.user().decreaseFollowing(userId = currentUserId)
        }
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

    override suspend fun unrecastContent(
        contentId: String,
        otherUserRecasted: Boolean,
        userId: String,
    ) {
        apiCall { api.unrecastContent(contentId = contentId, id = userId) }
        database.withTransaction {
            database.cast().decreaseRecastCount(contentId)
            database.cast().get(contentId, CastType.Recast, userId)?.also { existRecast ->
                database.cast().delete(existRecast.id)
                database.profile().deleteByOriginalCast(existRecast.id)
                database.search().deleteByOriginalCast(existRecast.id)
            }
            database.cast().updateRecasted(contentId, otherUserRecasted)
            database.user().decreaseCastCount(userId)
        }
    }

    override suspend fun updateEmail(email: String) {
        val response = apiCall { api.updateEmail(UpdateEmailRequest(email)) }
        val user = UserEntity.mapOwner(response)
        database.user().upsert(user)
    }

    override suspend fun updateUserProfile(userUpdateRequest: UploadImageRequest): Flow<BaseUiState<Nothing>> {
        return flow {
            apiCall {
                api.updateUserProfile(
                    castcleId = userUpdateRequest.currentCastcleId ?: "me", userUpdateRequest
                ).also {
                    if (it.isSuccessful) {
                        updateWhenUpdateSuccess(it.body())
                        emit(BaseUiState.SuccessNonBody)
                    } else {
                        emit(BaseUiState.Error(ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }
    }

    override suspend fun updateDetailProfile(userUpdateRequest: UserUpdateRequest):
        Flow<BaseUiState<Nothing>> {
        return flow {
            emit(BaseUiState.Loading(null, true))
            apiCall {
                api.updateDetailProfile(
                    castcleId = userUpdateRequest.currentCastcleId ?: "me", userUpdateRequest
                ).also {
                    emit(BaseUiState.Loading(null, false))
                    if (it.isSuccessful) {
                        updateWhenUpdateSuccess(it.body())
                        emit(BaseUiState.SuccessNonBody)
                    } else {
                        emit(BaseUiState.Error(ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }
    }

    private suspend fun updateWhenUpdateSuccess(response: UserResponse?) {
        val user = UserEntity.mapOwner(response)
        val linkSocial = LinkSocialEntity.map(response)
        val syncSocialUser = SyncSocialEntity.map(response)
        val syncSocialPage = SyncSocialEntity.map(response)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.linkSocial().delete()
            database.linkSocial().insert(linkSocial)
            database.syncSocial().delete()
            database.syncSocial().insert(syncSocialPage.plus(syncSocialUser))
            database.user().upsert(user)
        }
    }
}