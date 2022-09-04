package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutEntity

data class WalletSendShortcutViewEntity(
    val shortcut: WalletShortcutEntity? = null,
    val user: UserEntity = UserEntity(),
    override val uniqueId: String = "${R.layout.item_wallet_send_shortcut}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletSendShortcutViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletSendShortcutViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_send_shortcut

}