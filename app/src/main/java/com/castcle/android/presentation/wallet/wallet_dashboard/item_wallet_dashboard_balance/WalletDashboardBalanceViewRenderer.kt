package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDashboardBalanceBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.WalletDashboardListener
import io.reactivex.disposables.CompositeDisposable

class WalletDashboardBalanceViewRenderer : CastcleViewRenderer<WalletDashboardBalanceViewEntity,
    WalletDashboardBalanceViewHolder,
    WalletDashboardListener>(R.layout.item_wallet_dashboard_balance) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDashboardListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDashboardBalanceViewHolder(
        ItemWalletDashboardBalanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}