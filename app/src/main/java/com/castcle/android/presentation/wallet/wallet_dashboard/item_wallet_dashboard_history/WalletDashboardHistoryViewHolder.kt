package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.color
import com.castcle.android.core.extensions.toWalletTime
import com.castcle.android.databinding.ItemWalletDashboardHistoryBinding
import com.castcle.android.domain.wallet.type.WalletHistoryType
import java.text.NumberFormat

class WalletDashboardHistoryViewHolder(
    private val binding: ItemWalletDashboardHistoryBinding,
) : CastcleViewHolder<WalletDashboardHistoryViewEntity>(binding.root) {

    override var item = WalletDashboardHistoryViewEntity()

    override fun bind(bindItem: WalletDashboardHistoryViewEntity) {
        binding.tvTitle.text = item.history.type.name
        binding.tvDate.text = item.history.createdAt.toWalletTime()
        binding.tvStatus.text = item.history.status.name
        binding.tvStatus.isVisible = item.history.type is WalletHistoryType.Deposit
            || item.history.type is WalletHistoryType.Receive
            || item.history.type is WalletHistoryType.Send
            || item.history.type is WalletHistoryType.Withdraw
        binding.tvValue.text = item.history.value.toCastToken()
        binding.tvValue.setTextColor(
            when (item.history.type) {
                WalletHistoryType.Airdrop -> color(R.color.blue)
                WalletHistoryType.Deposit,
                WalletHistoryType.Receive -> color(R.color.green_1)
                WalletHistoryType.Farmed,
                WalletHistoryType.Farming,
                WalletHistoryType.Referral,
                WalletHistoryType.Social,
                WalletHistoryType.Unfarming -> color(R.color.white)
                WalletHistoryType.Send,
                WalletHistoryType.Withdraw -> color(R.color.red_4)
            }
        )
        binding.ivIcon.setImageResource(
            when (item.history.type) {
                WalletHistoryType.Airdrop -> R.drawable.ic_airdrop
                WalletHistoryType.Deposit,
                WalletHistoryType.Receive -> R.drawable.ic_status_receive
                WalletHistoryType.Farmed,
                WalletHistoryType.Farming,
                WalletHistoryType.Unfarming -> R.drawable.ic_content_farming
                WalletHistoryType.Referral,
                WalletHistoryType.Social -> R.drawable.ic_social_reward
                WalletHistoryType.Send,
                WalletHistoryType.Withdraw -> R.drawable.ic_status_send
            }
        )
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