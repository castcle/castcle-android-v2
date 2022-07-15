package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.feed.entity.FeedResponse
import retrofit2.Response
import retrofit2.http.*

interface FeedApi {

    @GET("feeds/guests")
    suspend fun getFeedGuest(
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
    ): Response<BaseResponse<FeedResponse>>

    @GET("feeds/members/{$PARAMETER_FEATURE_SLUG}/{$PARAMETER_CIRCLE_SLUG}")
    suspend fun getFeedMember(
        @Path(PARAMETER_FEATURE_SLUG) featureSlug: String,
        @Path(PARAMETER_CIRCLE_SLUG) circleSlug: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<FeedResponse>>

}