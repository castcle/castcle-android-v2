package com.castcle.android.data.core.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ApiErrorResponse(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("payload") val payload: ApiErrorPayloadResponse? = null,
    @SerializedName("statusCode") val statusCode: Int? = null,
)