package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ContentPhotoResponse(
    @SerializedName("contents") val contents: List<PhotoResponse>? = null,
)