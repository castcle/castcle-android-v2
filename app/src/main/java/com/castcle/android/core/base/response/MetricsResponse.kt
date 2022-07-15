package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MetricsResponse(
    @SerializedName("commentCount") val commentCount: Int? = null,
    @SerializedName("farmCount") val farmCount: Int? = null,
    @SerializedName("likeCount") val likeCount: Int? = null,
    @SerializedName("quoteCount") val quoteCount: Int? = null,
    @SerializedName("recastCount") val recastCount: Int? = null,
)