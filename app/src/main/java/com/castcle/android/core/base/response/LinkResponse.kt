package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LinkResponse(
    @SerializedName("description") val description: String? = null,
    @SerializedName("imagePreview") val imagePreview: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("url") val url: String? = null,
)