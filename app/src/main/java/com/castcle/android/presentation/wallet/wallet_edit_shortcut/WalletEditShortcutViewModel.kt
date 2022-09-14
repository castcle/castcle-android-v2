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

package com.castcle.android.presentation.wallet.wallet_edit_shortcut

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.wallet.entity.SortWalletShortcutRequest
import com.castcle.android.domain.wallet.WalletRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletEditShortcutViewModel(
    private val database: CastcleDatabase,
    private val mapper: WalletEditShortcutMapper,
    private val repository: WalletRepository,
    private val userId: String,
) : BaseViewModel() {

    val onDeleteSuccess = MutableSharedFlow<Int>()

    val onError = MutableSharedFlow<Throwable>()

    val onSaveSuccess = MutableSharedFlow<Unit>()

    val views = MutableLiveData<List<WalletEditShortcutAdapter.ViewEntity>>()

    init {
        getData()
    }

    fun deleteShortcut(position: Int, shortcutId: String) {
        launch(onError = {
            onError.emitOnSuspend(it)
        }, onSuccess = {
            onDeleteSuccess.emitOnSuspend(position)
        }) {
            repository.deleteWalletShortcut(shortcutId = shortcutId)
        }
    }

    private fun getData() {
        launch {
            views.postValue(mapper.map(database.walletShortcut().get(), userId))
        }
    }

    fun sortWalletShortcuts(items: MutableList<WalletEditShortcutAdapter.ViewEntity>) {
        launch(onError = {
            onError.emitOnSuspend(it)
        }, onSuccess = {
            onSaveSuccess.emitOnSuspend(it)
        }) {
            val body = items.mapIndexed { index, map ->
                SortWalletShortcutRequest.Payload(
                    id = map.shortcut.id,
                    order = index.plus(1),
                )
            }
            repository.sortWalletShortcuts(SortWalletShortcutRequest(body))
        }
    }

}