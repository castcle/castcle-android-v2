package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletBalanceEntity
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

data class WalletDashboardBalanceViewEntity(
    val balance: WalletBalanceEntity = WalletBalanceEntity(),
    val filter: WalletHistoryFilter = WalletHistoryFilter.WalletBalance,
    val user: UserEntity = UserEntity(),
    override val uniqueId: String = "${R.layout.item_wallet_dashboard_balance}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDashboardBalanceViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDashboardBalanceViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_dashboard_balance

}