package com.castcle.android.presentation.sign_up.create_profile.entity

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("castcleId") val castcleId: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("referral") val referral: String? = null,
)
