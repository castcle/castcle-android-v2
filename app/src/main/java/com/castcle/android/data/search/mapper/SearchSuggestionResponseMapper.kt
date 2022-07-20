package com.castcle.android.data.search.mapper

import com.castcle.android.data.search.entity.SearchSuggestionResponse
import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class SearchSuggestionResponseMapper {

    fun apply(response: SearchSuggestionResponse?) = SearchSuggestionEntity(
        hashtag = SearchSuggestionHashtagEntity.map(response?.hashtags).sortedBy { it.rank },
        keyword = SearchSuggestionKeywordEntity.map(response?.keyword),
        user = UserEntity.map(listOf(), response?.users),
    )

}