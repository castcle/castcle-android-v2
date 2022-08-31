package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

data class WalletDashboardDialogFilterViewEntity(
    val filter: WalletHistoryFilter = WalletHistoryFilter.WalletBalance,
    val selected: Boolean = false,
    override val uniqueId: String = "${R.layout.item_wallet_dashboard_dialog_filter}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDashboardDialogFilterViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDashboardDialogFilterViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_dashboard_dialog_filter

}