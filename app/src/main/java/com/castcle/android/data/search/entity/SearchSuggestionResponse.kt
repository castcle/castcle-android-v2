package com.castcle.android.data.search.entity

import androidx.annotation.Keep
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class SearchSuggestionResponse(
    @SerializedName("hashtags") val hashtags: List<HashtagsResponse>? = null,
    @SerializedName("keyword") val keyword: List<Keyword>? = null,
    @SerializedName("users") val users: List<UserResponse>? = null,
) {

    data class Keyword(
        @SerializedName("text") val text: String? = null,
    )

}