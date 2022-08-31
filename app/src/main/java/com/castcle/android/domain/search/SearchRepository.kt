package com.castcle.android.domain.search

import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.UserEntity

interface SearchRepository {
    suspend fun getHashtags(keyword: String): List<HashtagEntity>
    suspend fun getMentions(keyword: String): List<UserEntity>
    suspend fun getTopTrends(): List<SearchSuggestionHashtagEntity>
    suspend fun searchByKeyword(keyword: String): SearchSuggestionEntity
}