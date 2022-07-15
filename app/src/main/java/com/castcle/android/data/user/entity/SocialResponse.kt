package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SocialResponse(
    @SerializedName("active") val active: Boolean? = null,
    @SerializedName("autoPost") val autoPost: Boolean? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("socialId") val socialId: String? = null,
    @SerializedName("userName") val userName: String? = null,
)