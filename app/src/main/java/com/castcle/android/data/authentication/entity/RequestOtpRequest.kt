package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RequestOtpRequest(
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("mobileNumber") val mobileNumber: String? = null,
    @SerializedName("objective") val objective: String? = null,
)