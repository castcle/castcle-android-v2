package com.castcle.android.core.base.response

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("fullHd") val fullHd: String? = null,
    @SerializedName("large") val large: String? = null,
    @SerializedName("original") val original: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
)