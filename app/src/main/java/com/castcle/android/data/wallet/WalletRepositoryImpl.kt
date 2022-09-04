package com.castcle.android.data.wallet

import androidx.room.withTransaction
import com.castcle.android.core.api.WalletApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.domain.wallet.entity.*
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

@Factory
class WalletRepositoryImpl(
    private val api: WalletApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
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

    override suspend fun getWalletShortcuts(userId: String) {
        val accountId = database.accessToken().get()?.getAccountId().orEmpty()
        val response = apiCall { api.getWalletShortcuts(id = accountId) }
        val ownerUser = database.user().get()
        val result = response?.shortcuts.orEmpty().map { map ->
            val shortcut = WalletShortcutEntity(
                id = map.id.orEmpty(),
                order = map.order ?: 0,
                userId = map.userId.orEmpty(),
            )
            val user = ownerUser.find { it.id == map.userId } ?: UserEntity(
                avatar = ImageEntity.map(map.images?.avatar ?: map.avatar) ?: ImageEntity(),
                castcleId = map.castcleId.orEmpty(),
                displayName = map.displayName.orEmpty(),
                id = map.userId.orEmpty(),
                type = UserType.getFromId(map.type.orEmpty()),
            )
            shortcut to user
        }
        val shortcut = result.map { it.first }
        val user = result.map { it.second }
        delay(200)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.user().upsert(user)
            database.walletShortcut().delete()
            database.walletShortcut().insert(shortcut)
        }
    }

}