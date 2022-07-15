package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FollowUserRequest(
    @SerializedName("targetCastcleId") val targetCastcleId: String? = null,
)