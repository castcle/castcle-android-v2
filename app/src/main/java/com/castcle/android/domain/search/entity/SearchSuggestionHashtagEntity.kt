package com.castcle.android.domain.search.entity

import com.castcle.android.data.search.entity.HashtagsResponse

data class SearchSuggestionHashtagEntity(
    val count: Int = 0,
    val name: String = "",
    val rank: Int = 0,
    val slug: String = "",
) {

    companion object {
        fun map(response: List<HashtagsResponse>?) = response.orEmpty().map {
            SearchSuggestionHashtagEntity(
                count = it.count ?: 0,
                name = it.name ?: "",
                rank = it.rank ?: 0,
                slug = it.slug ?: "",
            )
        }
    }

}