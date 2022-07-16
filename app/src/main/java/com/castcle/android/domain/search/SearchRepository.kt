package com.castcle.android.domain.search

import com.castcle.android.domain.search.entity.TopTrendsEntity

interface SearchRepository {
    suspend fun getTopTrends(): List<TopTrendsEntity>
}