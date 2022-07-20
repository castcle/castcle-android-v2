package com.castcle.android.domain.search.entity

import com.castcle.android.domain.user.entity.UserEntity

data class SearchSuggestionEntity(
    val hashtag: List<SearchSuggestionHashtagEntity> = listOf(),
    val keyword: List<SearchSuggestionKeywordEntity> = listOf(),
    val user: List<UserEntity> = listOf(),
)