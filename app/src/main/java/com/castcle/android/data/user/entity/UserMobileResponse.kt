package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserMobileResponse(
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("number") val number: String? = null,
)