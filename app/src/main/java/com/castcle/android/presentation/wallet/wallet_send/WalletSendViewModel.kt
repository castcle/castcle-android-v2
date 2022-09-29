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

package com.castcle.android.presentation.wallet.wallet_send

import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.data.wallet.entity.WalletTransactionRequest
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewEntity
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletSendViewModel(
    private val database: CastcleDatabase,
    private val mapper: WalletSendMapper,
    private val parameter: WalletSendViewModelParameter,
    private val repository: WalletRepository,
) : BaseViewModel() {

    val loadState = MutableSharedFlow<LoadState>()

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<WalletTransactionRequest>()

    val views = MutableLiveData<WalletSendViewEntity>()

    init {
        fetchData()
        fetchWalletShortcut()
    }

    private fun fetchWalletShortcut() {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it) { fetchWalletShortcut() })
        }, onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            repository.getWalletShortcuts(userId = parameter.userId)
        }
    }

    private fun fetchData() {
        launch {
            database.user().retrieve(UserType.People)
                .combine(database.user().retrieve(UserType.Page)) { people, page ->
                    people.plus(page)
                }.combine(database.walletShortcut().retrieve()) { user, shortcut ->
                    mapper.apply(user, shortcut, parameter)
                }.combine(database.walletBalance().retrieve(parameter.userId)) { item, balance ->
                    item.copy(balance = balance?.availableBalance?.toDoubleOrNull() ?: 0.0)
                }.distinctUntilChanged().collectLatest {
                    views.postValue(it)
                }
        }
    }

    fun reviewTransaction() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val detail = WalletTransactionRequest.Detail(
                castcleId = views.value?.castcleId,
                targetCastcleId = views.value?.targetCastcleId,
                timestamp = System.currentTimeMillis(),
                userId = views.value?.userId.orEmpty(),
            )
            val transaction = WalletTransactionRequest.Transaction(
                address = views.value?.targetUserId,
                amount = views.value?.amount ?: 0.0,
                chainId = "castcle",
                memo = views.value?.memo?.ifBlank { null },
                note = views.value?.note?.ifBlank { null },
            )
            val body = WalletTransactionRequest(
                detail = detail,
                transaction = transaction,
            )
            repository.reviewTransaction(body = body)
        }
    }

    data class WalletSendViewModelParameter(
        val targetCastcleId: String? = null,
        val targetUserId: String? = null,
        val userId: String = "",
    )

}