package com.castcle.android.domain.search.entity

import com.castcle.android.data.search.entity.HashtagsResponse

data class HashtagEntity(
    val name: String = "",
    val slug: String = "",
) {

    companion object {
        fun map(response: List<HashtagsResponse>?) = response.orEmpty().map {
            HashtagEntity(
                name = it.name ?: "",
                slug = it.slug ?: "",
            )
        }
    }

}