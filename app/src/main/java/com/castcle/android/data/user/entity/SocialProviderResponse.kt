package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SocialProviderResponse(
    @SerializedName("facebook") val facebook: SocialResponse? = null,
    @SerializedName("google") val google: SocialResponse? = null,
    @SerializedName("twitter") val twitter: SocialResponse? = null,
)