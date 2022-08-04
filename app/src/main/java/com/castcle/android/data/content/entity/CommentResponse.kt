package com.castcle.android.data.content.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.MetricsResponse
import com.castcle.android.core.base.response.ParticipateResponse
import com.google.gson.annotations.SerializedName

@Keep
data class CommentResponse(
    @SerializedName("author") val author: String? = null,
    @SerializedName("authorId") val authorId: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("metrics") val metrics: MetricsResponse? = null,
    @SerializedName("participate") val participate: ParticipateResponse? = null,
    @SerializedName("reply") val reply: List<String>? = null,
)