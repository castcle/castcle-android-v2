package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReportRequest(
    @SerializedName("message") val message: String? = null,
    @SerializedName("subject") val subject: String? = null,
    @SerializedName("targetCastcleId") val targetCastcleId: String? = null,
    @SerializedName("targetContentId") val targetContentId: String? = null,
)