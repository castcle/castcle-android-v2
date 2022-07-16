package com.castcle.android.data.search

import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.TopTrendsEntity
import org.koin.core.annotation.Factory

@Factory
class SearchRepositoryImpl(
    private val api: SearchApi,
) : SearchRepository {

    override suspend fun getTopTrends(): List<TopTrendsEntity> {
        val response = apiCall { api.getTopTrends() }
        return TopTrendsEntity.map(response).sortedBy { it.rank }
    }

}