package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LikeCommentRequest(
    @SerializedName("commentId") val commentId: String? = null,
)