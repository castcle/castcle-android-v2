package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginWithSocialRequest(
    @SerializedName("authToken") val authToken: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("cover") val cover: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("link") val link: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("socialId") val socialId: String? = null,
)