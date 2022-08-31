package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RequestOtpResponse(
    @SerializedName("expiresTime") val expiresTime: String? = null,
    @SerializedName("objective") val objective: String? = null,
    @SerializedName("refCode") val refCode: String? = null,
)