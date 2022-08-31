package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDashboardDialogFilterBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogListener
import io.reactivex.disposables.CompositeDisposable

class WalletDashboardDialogFilterViewRenderer :
    CastcleViewRenderer<WalletDashboardDialogFilterViewEntity,
        WalletDashboardDialogFilterViewHolder,
        WalletDashboardDialogListener>(R.layout.item_wallet_dashboard_dialog_filter) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDashboardDialogListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDashboardDialogFilterViewHolder(
        ItemWalletDashboardDialogFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}