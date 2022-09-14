package com.castcle.android.presentation.wallet.wallet_edit_shortcut

import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutWithResultEntity
import org.koin.core.annotation.Factory

@Factory
class WalletEditShortcutMapper {

    fun map(
        shortcut: List<WalletShortcutWithResultEntity>,
        userId: String,
    ): List<WalletEditShortcutAdapter.ViewEntity> {
        return shortcut.map {
            WalletEditShortcutAdapter.ViewEntity(
                isYou = it.user?.id == userId,
                shortcut = it.shortcut,
                user = it.user ?: UserEntity(),
            )
        }
    }

}