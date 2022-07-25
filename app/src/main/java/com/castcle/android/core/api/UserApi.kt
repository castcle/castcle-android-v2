package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.user.entity.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

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
    ): Response<BaseResponse<UserResponse>>

    @GET("v2/users/{$PARAMETER_ID}/following")
    suspend fun getFollowing(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<UserResponse>>

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
    ): Response<BaseResponse<CastResponse>>

    @GET("v2/users/me/pages")
    suspend fun getUserPage(
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<UserResponse>>

    @GET("v2/users/me/suggestion-follow")
    suspend fun getWhoToFollow(
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<UserResponse>>

    @POST("v2/users/me/likes-casts")
    suspend fun likeCasts(
        @Body body: LikeCastsRequest,
    ): Response<Unit>

    @DELETE("v2/users/me/following/{targetCastcleId}")
    suspend fun unfollowUser(
        @Path("targetCastcleId") targetCastcleId: String,
    ): Response<Unit>

    @DELETE("v2/users/me/likes-casts/{contentId}")
    suspend fun unlikeCasts(
        @Path("contentId") contentId: String,
    ): Response<Unit>

}