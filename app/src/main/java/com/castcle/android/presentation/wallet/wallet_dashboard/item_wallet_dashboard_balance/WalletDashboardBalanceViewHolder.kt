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

package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletDashboardBalanceBinding
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.wallet.wallet_dashboard.WalletDashboardListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletDashboardBalanceViewHolder(
    private val binding: ItemWalletDashboardBalanceBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletDashboardListener,
) : CastcleViewHolder<WalletDashboardBalanceViewEntity>(binding.root) {

    override var item = WalletDashboardBalanceViewEntity()

    init {
        compositeDisposable += binding.ivInfo.onClick {
            listener.onInfoClicked(item.balance.totalBalance)
        }
        compositeDisposable += binding.viewSelectUser.onClick {
            listener.onSelectUserClicked(item.user.id)
        }
        compositeDisposable += binding.ivDeposit.onClick {
            listener.onDepositClicked(item.user.id)
        }
        compositeDisposable += binding.ivSend.onClick {
            listener.onSendClicked(item.user.id)
        }
        compositeDisposable += binding.ivAirdrop.onClick {
            listener.onAirdropClicked()
        }
        compositeDisposable += binding.ivFilter.onClick {
            listener.onFilterClicked(item.filter)
        }
    }

    override fun bind(bindItem: WalletDashboardBalanceViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvDisplayName.text = item.user.displayName
        binding.tvUserType.text = when (item.user.type) {
            is UserType.Page -> string(R.string.page)
            is UserType.People -> string(R.string.profile)
        }
        binding.liBalance.progress = if (item.balance.totalBalance.toDoubleOrNull() == 0.0) {
            50
        } else {
            val totalBalance = item.balance.totalBalance.toDoubleOrNull() ?: 0.0
            val farmBalance = item.balance.farmBalance.toDoubleOrNull() ?: 0.0
            if (totalBalance == 0.0 && farmBalance == 0.0) {
                50
            } else {
                100.div(totalBalance).times(farmBalance).toInt()
            }
        }
        binding.tvAvailableBalance.text = item.balance.availableBalance.withCastToken()
        binding.tvTotalBalance.text = item.balance.totalBalance.withCastToken()
        binding.tvFarmBalance.text = item.balance.farmBalance.withCastToken()
        binding.ivAirdrop.isVisible = item.airdropBannerEnable
        binding.tvFilter.text = item.filter.name
    }

    private fun String.withCastToken(): String {
        return "$this ${string(R.string.cast).uppercase()}"
    }

}