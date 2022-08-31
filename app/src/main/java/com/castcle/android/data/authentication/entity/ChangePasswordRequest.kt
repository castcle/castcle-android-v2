package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChangePasswordRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("newPassword") val newPassword: String? = null,
    @SerializedName("objective") val objective: String? = null,
    @SerializedName("refCode") val refCode: String? = null,
)