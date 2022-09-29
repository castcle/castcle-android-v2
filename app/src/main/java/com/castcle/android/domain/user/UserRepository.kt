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

package com.castcle.android.domain.user

import com.castcle.android.data.base.BaseUiState
import com.castcle.android.data.page.entity.SyncSocialRequest
import com.castcle.android.data.user.entity.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.update_profile.entity.UploadImageRequest
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
import com.twitter.sdk.android.core.TwitterAuthToken
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun commentCast(contentId: String, message: String)
    suspend fun createQuoteCast(body: CreateQuoteCastRequest, userId: String)
    suspend fun deleteAccount(body: DeleteAccountRequest)
    suspend fun deleteComment(commentId: String)
    suspend fun deleteReplyComment(replyCommentId: String)
    suspend fun disconnectWithSocial(syncSocialId: String, userId: String)
    suspend fun followUser(targetUser: UserEntity)
    suspend fun fetchUserPage(): List<UserEntity>
    suspend fun fetchUserProfile(): UserEntity
    suspend fun getUser(id: String): UserEntity
    suspend fun getUserFlow(id: String): Flow<UserEntity?>
    suspend fun likeCasts(content: CastEntity)
    suspend fun likeComment(comment: CommentEntity)
    suspend fun recastContent(contentId: String, userId: String)
    suspend fun replyComment(commentId: String, message: String)
    suspend fun reportContent(body: ReportRequest)
    suspend fun reportUser(body: ReportRequest)
    suspend fun syncWithFacebook(body: SyncSocialRequest, userId: String): Pair<Boolean, SyncSocialEntity>
    suspend fun syncWithTwitter(token: TwitterAuthToken?, userId: String): Pair<Boolean, SyncSocialEntity>
    suspend fun unfollowUser(targetUser: UserEntity)
    suspend fun unlikeCasts(content: CastEntity)
    suspend fun unlikeComment(comment: CommentEntity)
    suspend fun unrecastContent(contentId: String, otherUserRecasted: Boolean, userId: String)
    suspend fun updateAutoPost(enable: Boolean, syncSocialId: String, userId: String)
    suspend fun updateEmail(email: String)
    suspend fun updateUserProfile(userUpdateRequest: UploadImageRequest): Flow<BaseUiState<Nothing>>
    suspend fun updateDetailProfile(userUpdateRequest: UserUpdateRequest): Flow<BaseUiState<Nothing>>
    suspend fun updateProfileBirthDate(birthDate: String, userId: String)
}