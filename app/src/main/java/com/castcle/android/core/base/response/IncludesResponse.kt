package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class IncludesResponse(
    @SerializedName("casts") val casts: List<CastResponse>? = null,
    @SerializedName("users") val users: List<UserResponse>? = null,
)