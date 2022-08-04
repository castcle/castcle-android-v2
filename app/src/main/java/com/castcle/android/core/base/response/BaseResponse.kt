package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BaseResponse<T>(
    @SerializedName("includes") val includes: IncludesResponse? = null,
    @SerializedName("meta") val meta: MetaResponse? = null,
    @SerializedName("payload") val payload: T? = null,
)