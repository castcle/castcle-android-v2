/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.data.wallet

import androidx.room.withTransaction
import com.castcle.android.core.api.WalletApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.wallet.entity.WalletTransactionRequest
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

    override suspend fun confirmTransaction(body: WalletTransactionRequest) {
        apiCall { api.confirmTransaction(body = body, id = body.detail?.userId.orEmpty()) }
    }

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

    override suspend fun getWalletAddress(keyword: String, userId: String): List<UserEntity> {
        val response = if (keyword.isBlank()) {
            apiCall { api.getRecentWalletAddress(id = userId) }
        } else {
            apiCall { api.getWalletAddress(id = userId, keyword = keyword) }
        }
        val ownerUser = database.user().get().map { it.id }
        val user = UserEntity.map(ownerUser, response?.castcle)
        glidePreloader.loadUser(user)
        database.user().upsert(user)
        return user
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

    override suspend fun reviewTransaction(body: WalletTransactionRequest): WalletTransactionRequest {
        apiCall { api.reviewTransaction(body = body, id = body.detail?.userId.orEmpty()) }
        return body
    }

}