package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.search.entity.SearchSuggestionResponse
import com.castcle.android.data.user.entity.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("v2/searches/top-trends?limit=10")
    suspend fun getTopTrends(): Response<SearchSuggestionResponse>

    @GET("v2/searches/by")
    suspend fun searchByKeyword(
        @Query("keyword") keyword: String,
    ): Response<SearchSuggestionResponse>

    @GET("v2/feeds/search/recent")
    suspend fun searchLatest(
        @Query(PARAMETER_KEYWORD) keyword: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<CastResponse>>

    @GET("v2/feeds/search/trends")
    suspend fun searchTrend(
        @Query(PARAMETER_CONTENT_TYPE) contentType: String? = null,
        @Query(PARAMETER_KEYWORD) keyword: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<CastResponse>>

    @GET("v2/users/by")
    suspend fun searchUser(
        @Query(PARAMETER_KEYWORD) keyword: String,
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_UNTIL_ID) untilId: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<UserResponse>>

}