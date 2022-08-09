package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.content.entity.*
import retrofit2.Response
import retrofit2.http.*

interface ContentApi {

    @POST("v2/contents/feed")
    suspend fun createContent(
        @Body body: CreateContentRequest,
    ): Response<BaseResponse<CastResponse>>

    @GET("v2/contents/{$PARAMETER_CONTENT_ID}/comments")
    suspend fun getComment(
        @Path(PARAMETER_CONTENT_ID) contentId: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<CommentResponse>>>

    @GET("v2/contents/{$PARAMETER_CONTENT_ID}")
    suspend fun getContent(
        @Path(PARAMETER_CONTENT_ID) contentId: String,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<CastResponse>>

    @GET("v2/contents/{$PARAMETER_CONTENT_ID}/participates")
    suspend fun getContentParticipate(
        @Path(PARAMETER_CONTENT_ID) contentId: String,
    ): Response<List<ContentParticipateResponse>>

}