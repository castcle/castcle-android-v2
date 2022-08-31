package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

data class WalletDashboardEmptyViewEntity(
    val filter: WalletHistoryFilter = WalletHistoryFilter.WalletBalance,
    override val uniqueId: String = "${R.layout.item_wallet_dashboard_empty}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDashboardEmptyViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDashboardEmptyViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_dashboard_empty

}