package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AggregatorResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("type") val type: String? = null,
)