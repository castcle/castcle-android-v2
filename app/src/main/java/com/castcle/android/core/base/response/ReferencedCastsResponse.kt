package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReferencedCastsResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
)