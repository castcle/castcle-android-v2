/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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