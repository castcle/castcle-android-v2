package com.castcle.android.data.feed.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.*
import com.google.gson.annotations.SerializedName

@Keep
data class FeedResponse(
    @SerializedName("aggregator") val aggregator: AggregatorResponse? = null,
    @SerializedName("campaignMessage") val campaignMessage: String? = null,
    @SerializedName("campaignName") val campaignName: String? = null,
    @SerializedName("circle") val circle: CircleResponse? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("feature") val feature: FeatureResponse? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("payload") val payload: Any? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
)