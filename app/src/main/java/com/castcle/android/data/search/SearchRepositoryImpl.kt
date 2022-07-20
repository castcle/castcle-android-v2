package com.castcle.android.data.search

import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.search.mapper.SearchSuggestionResponseMapper
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.SearchSuggestionEntity
import com.castcle.android.domain.search.entity.SearchSuggestionHashtagEntity
import org.koin.core.annotation.Factory

@Factory
class SearchRepositoryImpl(
    private val api: SearchApi,
    private val glidePreloader: GlidePreloader,
    private val searchSuggestionResponseMapper: SearchSuggestionResponseMapper,
) : SearchRepository {

    override suspend fun getTopTrends(): List<SearchSuggestionHashtagEntity> {
        val response = apiCall { api.getTopTrends() }
        return searchSuggestionResponseMapper.apply(response).hashtag
    }

    override suspend fun searchByKeyword(keyword: String): SearchSuggestionEntity {
        val response = apiCall { api.searchByKeyword(keyword) }
        val result = searchSuggestionResponseMapper.apply(response)
        glidePreloader.loadUser(result.user)
        return result
    }

}