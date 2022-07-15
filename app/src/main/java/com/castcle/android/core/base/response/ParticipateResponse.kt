package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ParticipateResponse(
    @SerializedName("commented") val commented: Boolean? = null,
    @SerializedName("farming") val farming: Boolean? = null,
    @SerializedName("liked") val liked: Boolean? = null,
    @SerializedName("quoted") val quoted: Boolean? = null,
    @SerializedName("recasted") val recasted: Boolean? = null,
    @SerializedName("reported") val reported: Boolean? = null,
)