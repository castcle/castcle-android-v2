package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class WalletDashboardDialogUserViewEntity(
    val selected: Boolean = false,
    val user: UserEntity = UserEntity(),
    override val uniqueId: String = "${R.layout.item_wallet_dashboard_dialog_user}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDashboardDialogUserViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDashboardDialogUserViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_dashboard_dialog_user

}