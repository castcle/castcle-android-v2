package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserLinkResponse(
    @SerializedName("facebook") val facebook: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("twitter") val twitter: String? = null,
    @SerializedName("website") val website: String? = null,
    @SerializedName("youtube") val youtube: String? = null,
)