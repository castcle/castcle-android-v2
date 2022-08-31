package com.castcle.android.data.wallet.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WalletHistoryResponse(
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("value") val value: Double? = null,
)