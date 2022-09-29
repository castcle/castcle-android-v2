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

package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog.WalletDashboardDialogFilterViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog.WalletDashboardDialogUserViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletDashboardDialogViewModel(
    private val database: CastcleDatabase,
) : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    fun getItems(currentFilter: WalletHistoryFilter?, currentUserId: String?) {
        launch(onSuccess = {
            views.postValue(it)
        }) {
            if (currentFilter != null) {
                listOf(
                    WalletHistoryFilter.WalletBalance,
                    WalletHistoryFilter.ContentFarming,
                    WalletHistoryFilter.SocialRewards,
                    WalletHistoryFilter.DepositSend,
                    WalletHistoryFilter.AirdropReferral,
                    null
                ).map {
                    WalletDashboardDialogFilterViewEntity(
                        selected = currentFilter == it,
                        filter = it,
                        uniqueId = it?.id.orEmpty(),
                    )
                }
            } else {
                database.user().get(UserType.People)
                    .plus(database.user().get(UserType.Page))
                    .map {
                        WalletDashboardDialogUserViewEntity(
                            selected = it.id == currentUserId,
                            user = it,
                            uniqueId = it.id,
                        )
                    }
            }
        }
    }

}