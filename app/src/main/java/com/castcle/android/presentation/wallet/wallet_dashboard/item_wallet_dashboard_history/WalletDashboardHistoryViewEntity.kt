package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.wallet.entity.WalletHistoryEntity

data class WalletDashboardHistoryViewEntity(
    val history: WalletHistoryEntity = WalletHistoryEntity(),
    override val uniqueId: String = "${R.layout.item_wallet_dashboard_history}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDashboardHistoryViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDashboardHistoryViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_dashboard_history

}