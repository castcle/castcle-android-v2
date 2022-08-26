package com.castcle.android.data.metadata.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountryCodeResponse(
    @SerializedName("code") val code: String? = null,
    @SerializedName("dialCode") val dialCode: String? = null,
    @SerializedName("name") val name: String? = null,
)