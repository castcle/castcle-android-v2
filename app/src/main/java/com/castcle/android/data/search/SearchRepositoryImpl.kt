package com.castcle.android.data.search

import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.search.mapper.SearchSuggestionResponseMapper
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class SearchRepositoryImpl(
    private val api: SearchApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val searchSuggestionResponseMapper: SearchSuggestionResponseMapper,
) : SearchRepository {

    override suspend fun getHashtags(keyword: String): List<HashtagEntity> {
        val response = apiCall { api.hashtagLookup(keyword) }
        return HashtagEntity.map(response?.payload)
    }

    override suspend fun getMentions(keyword: String): List<UserEntity> {
        val response = apiCall { api.mentionsLookup(keyword) }
        val ownerUserId = database.user().get().map { it.id }
        val user = UserEntity.map(ownerUserId, response?.payload)
        glidePreloader.loadUser(user)
        database.user().upsert(user)
        return user
    }

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