package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RequestOtpMobileRequest(
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("objective") val objective: String? = "verify_mobile",
    @SerializedName("mobileNumber") val mobileNumber: String? = null,
)