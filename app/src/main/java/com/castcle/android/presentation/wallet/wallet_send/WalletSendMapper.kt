package com.castcle.android.presentation.wallet.wallet_send

import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutWithResultEntity
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewEntity
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut.WalletSendShortcutViewEntity
import org.koin.core.annotation.Factory

@Factory
class WalletSendMapper {

    fun apply(
        account: List<UserEntity>,
        shortcut: List<WalletShortcutWithResultEntity>,
        parameter: WalletSendViewModel.WalletSendViewModelParameter,
    ): WalletSendViewEntity {
        val accountItems = account.filter { it.id != parameter.userId }.map {
            WalletSendShortcutViewEntity(
                uniqueId = "|${it.id}",
                user = it,
            )
        }
        val shortcutItems = shortcut.map {
            WalletSendShortcutViewEntity(
                shortcut = it.shortcut,
                uniqueId = "${it.shortcut.id}|${it.user}",
                user = it.user ?: UserEntity(),
            )
        }
        return WalletSendViewEntity(
            shortcut = accountItems.plus(shortcutItems),
            targetCastcleId = parameter.targetCastcleId.orEmpty(),
            targetUserId = parameter.targetUserId.orEmpty(),
            userId = parameter.userId,
        )
    }

}