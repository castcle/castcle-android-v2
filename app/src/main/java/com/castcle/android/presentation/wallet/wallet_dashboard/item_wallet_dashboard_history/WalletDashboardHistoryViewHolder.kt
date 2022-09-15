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

package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletDashboardHistoryBinding
import com.castcle.android.domain.wallet.type.WalletHistoryType
import java.text.NumberFormat

class WalletDashboardHistoryViewHolder(
    private val binding: ItemWalletDashboardHistoryBinding,
) : CastcleViewHolder<WalletDashboardHistoryViewEntity>(binding.root) {

    override var item = WalletDashboardHistoryViewEntity()

    override fun bind(bindItem: WalletDashboardHistoryViewEntity) {
        binding.tvTitle.text = item.history.type.name
        binding.tvStatus.text = item.history.status.name
        binding.tvDate.text = item.history.createdAt.toWalletTime()
        binding.tvValue.text = if (item.history.type is WalletHistoryType.Farming) {
            "-${item.history.value.toCastToken()}"
        } else {
            item.history.value.toCastToken()
        }
        binding.tvValue.setTextColor(
            when (item.history.type) {
                WalletHistoryType.Airdrop,
                WalletHistoryType.ContentReach,
                WalletHistoryType.Farmed,
                WalletHistoryType.Referral,
                WalletHistoryType.SeenAds,
                WalletHistoryType.Social,
                WalletHistoryType.Unfarming -> color(R.color.blue)
                WalletHistoryType.Deposit,
                WalletHistoryType.Receive -> color(R.color.green_1)
                WalletHistoryType.Farming,
                WalletHistoryType.Send,
                WalletHistoryType.Withdraw -> color(R.color.red_4)
            }
        )
        binding.ivIcon.setImageResource(
            when (item.history.type) {
                WalletHistoryType.Airdrop,
                WalletHistoryType.Referral -> R.drawable.ic_airdrop
                WalletHistoryType.Deposit,
                WalletHistoryType.Receive -> R.drawable.ic_status_receive
                WalletHistoryType.ContentReach,
                WalletHistoryType.Farmed,
                WalletHistoryType.Farming,
                WalletHistoryType.SeenAds,
                WalletHistoryType.Unfarming -> R.drawable.ic_content_farming
                WalletHistoryType.Social -> R.drawable.ic_social_reward
                WalletHistoryType.Send,
                WalletHistoryType.Withdraw -> R.drawable.ic_status_send
            }
        )
        binding.ivIcon.imageTintList = when (item.history.type) {
            WalletHistoryType.Airdrop,
            WalletHistoryType.ContentReach,
            WalletHistoryType.Farmed,
            WalletHistoryType.Referral,
            WalletHistoryType.SeenAds,
            WalletHistoryType.Social -> colorStateList(R.color.white)
            WalletHistoryType.Deposit,
            WalletHistoryType.Receive -> colorStateList(R.color.green_1)
            WalletHistoryType.Farming -> colorStateList(R.color.blue)
            WalletHistoryType.Send,
            WalletHistoryType.Unfarming,
            WalletHistoryType.Withdraw -> colorStateList(R.color.red_4)
        }
    }

    private fun Double.toCastToken(): String {
        val fractionDigits = when {
            this <= 0 -> 0
            this > 0 && this < 100 -> 8
            this >= 100 && this < 100_000 -> 6
            this >= 100_000 && this < 10_000_000 -> 3
            else -> 2
        }
        val numberFormat = NumberFormat.getNumberInstance().apply {
            minimumFractionDigits = fractionDigits
            maximumFractionDigits = fractionDigits
        }
        return "${numberFormat.format(this)} CAST"
    }

}