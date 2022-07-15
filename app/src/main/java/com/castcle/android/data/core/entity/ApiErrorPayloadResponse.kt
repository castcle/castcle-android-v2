package com.castcle.android.data.core.entity

import androidx.annotation.Keep
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class ApiErrorPayloadResponse(
    @SerializedName("profile") val profile: UserResponse? = null
)