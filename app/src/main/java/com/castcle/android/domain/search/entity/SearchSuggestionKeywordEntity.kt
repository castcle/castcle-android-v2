package com.castcle.android.domain.search.entity

import com.castcle.android.data.search.entity.SearchSuggestionResponse

data class SearchSuggestionKeywordEntity(
    val text: String = "",
) {

    companion object {
        fun map(response: List<SearchSuggestionResponse.Keyword>?) = response.orEmpty().map {
            SearchSuggestionKeywordEntity(text = it.text.orEmpty())
        }
    }

}