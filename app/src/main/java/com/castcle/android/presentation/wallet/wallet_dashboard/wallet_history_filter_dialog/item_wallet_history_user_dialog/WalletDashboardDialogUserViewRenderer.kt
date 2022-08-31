package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDashboardDialogUserBinding
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogListener
import io.reactivex.disposables.CompositeDisposable

class WalletDashboardDialogUserViewRenderer :
    CastcleViewRenderer<WalletDashboardDialogUserViewEntity,
        WalletDashboardDialogUserViewHolder,
        WalletDashboardDialogListener>(R.layout.item_wallet_dashboard_dialog_user) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDashboardDialogListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDashboardDialogUserViewHolder(
        ItemWalletDashboardDialogUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}