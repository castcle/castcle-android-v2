package com.castcle.android.domain.search

import com.castcle.android.domain.search.entity.SearchSuggestionEntity
import com.castcle.android.domain.search.entity.SearchSuggestionHashtagEntity

interface SearchRepository {
    suspend fun getTopTrends(): List<SearchSuggestionHashtagEntity>
    suspend fun searchByKeyword(keyword: String): SearchSuggestionEntity
}