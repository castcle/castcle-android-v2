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

package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.content.entity.CommentResponse
import com.castcle.android.data.user.entity.*
import com.castcle.android.presentation.sign_up.update_profile.entity.UploadImageRequest
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("v2/users/me/comments")
    suspend fun commentCast(
        @Body body: CommentRequest,
    ): Response<BaseResponse<CommentResponse>>

    @POST("v2/users/{$PARAMETER_ID}/quotecasts")
    suspend fun createQuoteCast(
        @Path(PARAMETER_ID) id: String,
        @Body body: CreateQuoteCastRequest,
    ): Response<BaseResponse<CastResponse>>

    @HTTP(method = "DELETE", path = "v2/users/me", hasBody = true)
    suspend fun deleteAccount(
        @Body body: DeleteAccountRequest,
    ): Response<Response<Unit>>

    @DELETE("v2/users/me/comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: String,
    ): Response<Unit>

    @DELETE("/v2/users/me/comments/{commentId}/reply/{replyCommentId}")
    suspend fun deleteReplyComment(
        @Path("commentId") commentId: String,
        @Path("replyCommentId") replyCommentId: String,
    ): Response<Unit>

    @POST("v2/users/me/following")
    suspend fun followUser(
        @Body body: FollowUserRequest,
    ): Response<Unit>

    @GET("v2/users/{$PARAMETER_ID}/followers")
    suspend fun getFollowers(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<UserResponse>>>

    @GET("v2/users/{$PARAMETER_ID}/following")
    suspend fun getFollowing(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<UserResponse>>>

    @GET("v2/users/{$PARAMETER_ID}")
    suspend fun getUser(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<UserResponse>

    @GET("v2/users/{$PARAMETER_ID}/contents")
    suspend fun getUserCast(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<CastResponse>>>

    @GET("v2/users/me/pages")
    suspend fun getUserPage(
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<UserResponse>>>

    @GET("v2/users/me/suggestion-follow")
    suspend fun getWhoToFollow(
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<UserResponse>>>

    @POST("v2/users/me/likes-casts")
    suspend fun likeCasts(
        @Body body: LikeCastsRequest,
    ): Response<Unit>

    @POST("v2/users/me/likes-comments")
    suspend fun likeComment(
        @Body body: LikeCommentRequest,
    ): Response<Unit>

    @POST("v2/users/{$PARAMETER_ID}/recasts")
    suspend fun recastContent(
        @Path(PARAMETER_ID) id: String,
        @Body body: RecastContentRequest,
    ): Response<BaseResponse<CastResponse>>

    @POST("v2/users/me/comments/{commentId}/reply")
    suspend fun replyComment(
        @Path("commentId") commentId: String,
        @Body body: CommentRequest,
    ): Response<BaseResponse<CommentResponse>>

    @POST("v2/users/me/reporting/content")
    suspend fun reportContent(
        @Body body: ReportRequest,
    ): Response<Unit>

    @POST("v2/users/me/reporting/user")
    suspend fun reportUser(
        @Body body: ReportRequest,
    ): Response<Unit>

    @DELETE("v2/users/me/following/{targetCastcleId}")
    suspend fun unfollowUser(
        @Path("targetCastcleId") targetCastcleId: String,
    ): Response<Unit>

    @DELETE("v2/users/me/likes-casts/{contentId}")
    suspend fun unlikeCasts(
        @Path("contentId") contentId: String,
    ): Response<Unit>

    @DELETE("v2/users/me/likes-comments/{commentId}")
    suspend fun unlikeComment(
        @Path("commentId") commentId: String,
    ): Response<Unit>

    @DELETE("v2/users/{$PARAMETER_ID}/recasts/{${PARAMETER_CONTENT_ID}}")
    suspend fun unrecastContent(
        @Path(PARAMETER_ID) id: String,
        @Path(PARAMETER_CONTENT_ID) contentId: String,
    ): Response<Unit>

    @PUT("v2/users/me/email")
    suspend fun updateEmail(
        @Body body: UpdateEmailRequest
    ): Response<UserResponse>

    @PUT("v2/users/{castcleId}")
    suspend fun updateUserProfile(
        @Path("castcleId") castcleId: String,
        @Body uploadImageRequest: UploadImageRequest
    ): Response<UserResponse>

    @PUT("v2/users/{castcleId}")
    suspend fun updateDetailProfile(
        @Path("castcleId") castcleId: String,
        @Body uploadImageRequest: UserUpdateRequest
    ): Response<UserResponse>
}