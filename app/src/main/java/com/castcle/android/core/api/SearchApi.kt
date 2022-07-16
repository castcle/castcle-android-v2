package com.castcle.android.core.api

import com.castcle.android.data.search.entity.SearchResponse
import retrofit2.Response
import retrofit2.http.GET

interface SearchApi {

    @GET("v2/searches/top-trends?limit=10")
    suspend fun getTopTrends(): Response<SearchResponse>

}