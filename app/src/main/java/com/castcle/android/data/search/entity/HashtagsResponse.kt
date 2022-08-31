package com.castcle.android.data.search.entity

import com.google.gson.annotations.SerializedName

data class HashtagsResponse(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("rank") val rank: Int? = null,
    @SerializedName("slug") val slug: String? = null,
)