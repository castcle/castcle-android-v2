package com.castcle.android.data.wallet.entity

import androidx.annotation.Keep
import com.castcle.android.data.user.entity.UserResponse
import com.google.gson.annotations.SerializedName

@Keep
data class WalletShortcutsResponse(
    @SerializedName("shortcuts") val shortcuts: List<UserResponse>? = null,
)