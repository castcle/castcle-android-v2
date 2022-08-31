package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletDashboardDialogFilterBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletDashboardDialogFilterViewHolder(
    private val binding: ItemWalletDashboardDialogFilterBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletDashboardDialogListener,
) : CastcleViewHolder<WalletDashboardDialogFilterViewEntity>(binding.root) {

    override var item = WalletDashboardDialogFilterViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onFilterClicked(item.filter)
        }
    }

    override fun bind(bindItem: WalletDashboardDialogFilterViewEntity) {
        binding.tvTitle.text = item.filter.name
        binding.tvTitle.setTextColor(
            if (item.selected) {
                color(R.color.blue)
            } else {
                color(R.color.white)
            }
        )
        binding.root.background = if (bindingAdapterPosition == 0) {
            drawable(R.drawable.bg_corner_top_16dp)
        } else {
            drawable(R.drawable.bg_rectangle)
        }
    }

}