package com.castcle.android.domain.wallet

import com.castcle.android.domain.wallet.entity.WalletBalanceEntity
import com.castcle.android.domain.wallet.entity.WalletHistoryEntity

interface WalletRepository {
    suspend fun getMyQrCode(userId: String): String
    suspend fun getWalletBalance(userId: String): WalletBalanceEntity
    suspend fun getWalletHistory(filter: String, userId: String): List<WalletHistoryEntity>
    suspend fun getWalletShortcuts(userId: String)
}