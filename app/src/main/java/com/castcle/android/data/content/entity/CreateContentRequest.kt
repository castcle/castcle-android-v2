package com.castcle.android.data.content.entity

import androidx.annotation.Keep
import com.castcle.android.domain.cast.type.CastType
import com.google.gson.annotations.SerializedName

@Keep
data class CreateContentRequest(
    @SerializedName("castcleId") val castcleId: String? = null,
    @SerializedName("payload") val payload: Payload? = null,
    @SerializedName("type") val type: String = CastType.Short.id,
) {

    data class Contents(
        @SerializedName("image") val image: String? = null,
    )

    data class Payload(
        @SerializedName("message") val message: String? = null,
        @SerializedName("photo") val photo: Photo? = null,
    )

    data class Photo(
        @SerializedName("contents") val contents: List<Contents>? = null,
    )

}