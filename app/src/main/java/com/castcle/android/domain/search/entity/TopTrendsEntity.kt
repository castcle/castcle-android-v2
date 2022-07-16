package com.castcle.android.domain.search.entity

import com.castcle.android.data.search.entity.SearchResponse

data class TopTrendsEntity(
    val count: Int = 0,
    val name: String = "",
    val rank: Int = 0,
    val slug: String = "",
) {

    companion object {
        fun map(response: SearchResponse?) = response?.hashtags.orEmpty().map {
            TopTrendsEntity(
                count = it.count ?: 0,
                name = it.name ?: "",
                rank = it.rank ?: 0,
                slug = it.slug ?: "",
            )
        }
    }

}