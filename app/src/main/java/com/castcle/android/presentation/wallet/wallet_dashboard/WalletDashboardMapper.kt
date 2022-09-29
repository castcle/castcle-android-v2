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

package com.castcle.android.presentation.wallet.wallet_dashboard

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_append.ErrorAppendViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_append.LoadingAppendViewEntity
import com.castcle.android.domain.setting.entity.ConfigEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.*
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance.WalletDashboardBalanceViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty.WalletDashboardEmptyViewEntity
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history.WalletDashboardHistoryViewEntity
import org.koin.core.annotation.Factory

@Factory
class WalletDashboardMapper {

    fun apply(
        response: List<WalletDashboardWithResultEntity>,
        retryAction: () -> Unit,
        config: ConfigEntity?,
    ): List<CastcleViewEntity> {
        return response.map { map ->
            when (map.dashboard.type) {
                is WalletDashboardType.Balance -> WalletDashboardBalanceViewEntity(
                    airdropBannerEnable = config?.airdropBannerEnable ?: false,
                    balance = map.balance ?: WalletBalanceEntity(),
                    filter = map.dashboard.filter,
                    user = map.user ?: UserEntity(),
                )
                is WalletDashboardType.Empty -> WalletDashboardEmptyViewEntity(
                    filter = map.dashboard.filter,
                )
                is WalletDashboardType.Error -> ErrorAppendViewEntity(
                    action = retryAction,
                    errorMessage = map.dashboard.relationId,
                )
                is WalletDashboardType.Loading -> LoadingAppendViewEntity()
                is WalletDashboardType.History -> WalletDashboardHistoryViewEntity(
                    history = map.history ?: WalletHistoryEntity(),
                    uniqueId = map.history?.id.orEmpty(),
                )
            }
        }
    }

}