package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetGuestAccessTokenRequest(
    @SerializedName("deviceUUID") val deviceUUID: String? = null,
)