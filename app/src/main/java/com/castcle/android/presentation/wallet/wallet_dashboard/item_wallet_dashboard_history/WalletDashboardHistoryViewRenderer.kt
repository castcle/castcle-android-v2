package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDashboardHistoryBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.WalletDashboardListener
import io.reactivex.disposables.CompositeDisposable

class WalletDashboardHistoryViewRenderer : CastcleViewRenderer<WalletDashboardHistoryViewEntity,
    WalletDashboardHistoryViewHolder,
    WalletDashboardListener>(R.layout.item_wallet_dashboard_history) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDashboardListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDashboardHistoryViewHolder(
        ItemWalletDashboardHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}