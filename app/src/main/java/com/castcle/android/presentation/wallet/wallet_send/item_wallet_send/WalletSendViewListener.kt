package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send

import com.castcle.android.core.base.recyclerview.CastcleListener

interface WalletSendViewListener : CastcleListener {
    fun onAddShortcutClicked()
    fun onShortcutClicked(castcleId: String, userId: String)
}