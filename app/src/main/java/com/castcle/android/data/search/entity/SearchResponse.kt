package com.castcle.android.data.search.entity

import androidx.annotation.Keep
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class SearchResponse(
    @SerializedName("hashtags") val hashtags: List<Hashtags>? = null,
    @SerializedName("keyword") val keyword: List<Keyword>? = null,
    @SerializedName("users") val users: List<UserResponse>? = null,
) {

    data class Hashtags(
        @SerializedName("count") val count: Int? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("rank") val rank: Int? = null,
        @SerializedName("slug") val slug: String? = null,
    )

    data class Keyword(
        @SerializedName("text") val text: String? = null,
    )

}