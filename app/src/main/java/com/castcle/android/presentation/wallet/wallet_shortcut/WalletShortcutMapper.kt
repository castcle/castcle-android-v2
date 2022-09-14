package com.castcle.android.presentation.wallet.wallet_shortcut

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutWithResultEntity
import com.castcle.android.presentation.wallet.wallet_shortcut.item_wallet_shortcut.WalletShortcutViewEntity
import com.castcle.android.presentation.wallet.wallet_shortcut.item_wallet_shortcut_title.WalletShortcutTitleViewEntity
import org.koin.core.annotation.Factory

@Factory
class WalletShortcutMapper {

    fun map(
        shortcut: List<WalletShortcutWithResultEntity>,
        user: List<UserEntity>,
        userId: String,
    ): List<CastcleViewEntity> {
        val myAccountTitleItems = WalletShortcutTitleViewEntity(isMyAccount = true)
        val myAccountItems = user.map {
            WalletShortcutViewEntity(
                isYou = userId == it.id,
                uniqueId = it.id,
                user = it,
            )
        }
        val shortcutTitleItems = WalletShortcutTitleViewEntity(isMyAccount = false)
        val shortcutItems = shortcut.map {
            WalletShortcutViewEntity(
                isYou = userId == it.user?.id,
                uniqueId = it.shortcut.id,
                user = it.user ?: UserEntity(),
            )
        }
        return listOf(myAccountTitleItems)
            .plus(myAccountItems)
            .plus(shortcutTitleItems)
            .plus(shortcutItems)
    }

}