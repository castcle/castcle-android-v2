package com.castcle.android.core.api

import com.castcle.android.data.search.entity.SearchSuggestionResponse
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

}