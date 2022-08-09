package com.castcle.android.data.content.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.ParticipateResponse
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class ContentParticipateResponse(
    @SerializedName("participate") val participate: ParticipateResponse? = null,
    @SerializedName("user") val user: UserResponse? = null,
)