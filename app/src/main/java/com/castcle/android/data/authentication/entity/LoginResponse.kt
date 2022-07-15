package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String? = null,
    @SerializedName("profile") val profile: UserResponse? = null,
    @SerializedName("pages") val pages: List<UserResponse>? = null,
    @SerializedName("refreshToken") val refreshToken: String? = null,
    @SerializedName("registered") val registered: Boolean? = null,
)