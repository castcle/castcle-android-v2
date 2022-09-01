package com.castcle.android.data.wallet

import com.castcle.android.core.api.WalletApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.domain.wallet.entity.WalletBalanceEntity
import com.castcle.android.domain.wallet.entity.WalletHistoryEntity
import org.koin.core.annotation.Factory

@Factory
class WalletRepositoryImpl(
    private val api: WalletApi,
    private val database: CastcleDatabase,
) : WalletRepository {

    override suspend fun getMyQrCode(userId: String): String {
        return try {
            apiCall { api.getMyQrCode(id = userId) }
                ?.payload
                ?.let { it.substring(it.indexOf(",") + 1) }
                ?: ""
        } catch (exception: Exception) {
            ""
        }
    }

    override suspend fun getWalletBalance(userId: String): WalletBalanceEntity {
        val response = apiCall { api.getWalletBalance(id = userId) }
        val walletBalance = WalletBalanceEntity.map(response)
        database.walletBalance().insert(walletBalance)
        return walletBalance
    }

    override suspend fun getWalletHistory(
        filter: String,
        userId: String,
    ): List<WalletHistoryEntity> {
        val response = apiCall { api.getWalletHistory(filter = filter, id = userId) }
        val walletHistory = WalletHistoryEntity.map(response)
        database.walletHistory().delete()
        database.walletHistory().insert(walletHistory)
        return walletHistory
    }

}