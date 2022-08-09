package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RecastContentRequest(
    @SerializedName("contentId") val contentId: String? = null,
)