package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDashboardEmptyBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.WalletDashboardListener
import io.reactivex.disposables.CompositeDisposable

class WalletDashboardEmptyViewRenderer : CastcleViewRenderer<WalletDashboardEmptyViewEntity,
    WalletDashboardEmptyViewHolder,
    WalletDashboardListener>(R.layout.item_wallet_dashboard_empty) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDashboardListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDashboardEmptyViewHolder(
        ItemWalletDashboardEmptyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}