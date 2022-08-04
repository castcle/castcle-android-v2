package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CommentRequest(
    @SerializedName("contentId") val contentId: String? = null,
    @SerializedName("message") val message: String? = null,
)