package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance

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
        binding.tvFilter.text = item.filter.name
    }

    private fun String.withCastToken(): String {
        return "$this ${string(R.string.cast).uppercase()}"
    }

}