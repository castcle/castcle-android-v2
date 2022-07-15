package com.castcle.android.data.cast.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.*
import com.google.gson.annotations.SerializedName

@Keep
data class CastResponse(
    @SerializedName("authorId") val authorId: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("link") val link: List<LinkResponse>? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("metrics") val metrics: MetricsResponse? = null,
    @SerializedName("participate") val participate: ParticipateResponse? = null,
    @SerializedName("photo") val photo: ContentPhotoResponse? = null,
    @SerializedName("referencedCasts") val referencedCasts: ReferencedCastsResponse? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
)