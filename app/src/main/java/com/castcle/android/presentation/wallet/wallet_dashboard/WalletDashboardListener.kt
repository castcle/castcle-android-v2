package com.castcle.android.presentation.wallet.wallet_dashboard

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

interface WalletDashboardListener : CastcleListener {
    fun onAirdropClicked()
    fun onDepositClicked(currentUserId: String)
    fun onFilterClicked(currentFilter: WalletHistoryFilter)
    fun onSelectUserClicked(currentUserId: String)
    fun onSendClicked(currentUserId: String)
}