package com.castcle.android.data.authentication.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginWithEmailRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
)