package com.castcle.android.data.metadata.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReportSubjectResponse(
    @SerializedName("name") val name: String? = null,
    @SerializedName("order") val order: Int? = null,
    @SerializedName("slug") val slug: String? = null,
)