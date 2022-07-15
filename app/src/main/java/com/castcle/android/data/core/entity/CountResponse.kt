package com.castcle.android.data.core.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountResponse(
    @SerializedName("count") val count: Int? = null,
)