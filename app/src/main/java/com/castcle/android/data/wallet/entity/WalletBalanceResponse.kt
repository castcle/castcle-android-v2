package com.castcle.android.data.wallet.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WalletBalanceResponse(
    @SerializedName("adCredit") val adCredit: String? = null,
    @SerializedName("availableBalance") val availableBalance: String? = null,
    @SerializedName("farmBalance") val farmBalance: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("totalBalance") val totalBalance: String? = null,
)