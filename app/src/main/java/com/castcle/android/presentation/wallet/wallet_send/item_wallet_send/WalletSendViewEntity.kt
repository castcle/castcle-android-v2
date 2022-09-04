package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut.WalletSendShortcutViewEntity

data class WalletSendViewEntity(
    var amount: Double = 0.0,
    val balance: Double = 0.0,
    var memo: String = "",
    var note: String = "",
    var targetCastcleId: String = "",
    var targetUserId: String = "",
    val shortcut: List<WalletSendShortcutViewEntity> = listOf(),
    val userId: String = "",
    override val uniqueId: String = "${R.layout.item_wallet_send}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletSendViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletSendViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_send

}