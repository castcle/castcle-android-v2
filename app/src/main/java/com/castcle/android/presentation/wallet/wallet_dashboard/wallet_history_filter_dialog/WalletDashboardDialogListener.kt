package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

interface WalletDashboardDialogListener : CastcleListener {
    fun onFilterClicked(filter: WalletHistoryFilter)
    fun onUserClicked(user: UserEntity)
}