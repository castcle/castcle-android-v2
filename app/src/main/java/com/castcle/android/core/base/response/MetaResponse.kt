package com.castcle.android.core.base.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MetaResponse(
    @SerializedName("newestId") val newestId: String? = null,
    @SerializedName("nextToken") val nextToken: String? = null,
    @SerializedName("oldestId") val oldestId: String? = null,
    @SerializedName("resultCount") val resultCount: Int? = null,
) {

    fun getNextLoadKey(currentLoadKey: String?) = when (oldestId) {
        null, currentLoadKey, "" -> null
        else -> oldestId
    }

}