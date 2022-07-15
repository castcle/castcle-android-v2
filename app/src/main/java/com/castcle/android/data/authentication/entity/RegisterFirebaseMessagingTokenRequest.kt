package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegisterFirebaseMessagingTokenRequest(
    @SerializedName("firebaseToken") val firebaseToken: String? = null,
    @SerializedName("platform") val platform: String? = "android",
    @SerializedName("uuid") val uuid: String? = null,
)