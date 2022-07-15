package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserContactResponse(
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
)