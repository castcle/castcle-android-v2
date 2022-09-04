package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_add_shortcut

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class WalletSendAddShortcutViewEntity(
    override val uniqueId: String = "${R.layout.item_wallet_send_add_shortcut}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletSendAddShortcutViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletSendAddShortcutViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_send_add_shortcut

}