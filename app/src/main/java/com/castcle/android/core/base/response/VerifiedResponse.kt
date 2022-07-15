package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifiedResponse(
    @SerializedName("email") val email: Boolean? = null,
    @SerializedName("mobile") val mobile: Boolean? = null,
    @SerializedName("official") val official: Boolean? = null,
    @SerializedName("social") val social: Boolean? = null,
)