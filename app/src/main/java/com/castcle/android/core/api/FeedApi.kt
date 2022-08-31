package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.feed.entity.FeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {

    @GET("v2/feeds/recent/forYou")
    suspend fun getFeed(
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_NEXT_TOKEN) nextToken: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<FeedResponse>>>

}