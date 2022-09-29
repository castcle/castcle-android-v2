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

package com.castcle.android.presentation.wallet.wallet_add_shortcut.item_wallet_add_shortcut

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletAddShortcutBinding
import com.castcle.android.presentation.wallet.wallet_add_shortcut.WalletAddShortcutListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletAddShortcutViewHolder(
    private val binding: ItemWalletAddShortcutBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletAddShortcutListener,
) : CastcleViewHolder<WalletAddShortcutViewEntity>(binding.root) {

    override var item = WalletAddShortcutViewEntity()

    init {
        compositeDisposable += binding.tvCastcleId.onClick {
            listener.onCastcleIdClicked()
        }
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onCreateShortcut(item.userId)
        }
        compositeDisposable += binding.ivScan.onClick {
            listener.onScanQrCodeClicked()
        }
    }

    override fun bind(bindItem: WalletAddShortcutViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.avatar)
        binding.tvCastcleId.text = item.castcleId
        binding.tvName.text = item.castcleId
        binding.groupName.isVisible = item.castcleId.isNotBlank() && item.userId.isNotBlank()
        binding.tvConfirm.isEnabled = item.castcleId.isNotBlank() && item.userId.isNotBlank()
        binding.tvConfirm.backgroundTintList =
            if (item.castcleId.isNotBlank() && item.userId.isNotBlank()) {
                colorStateList(R.color.blue)
            } else {
                colorStateList(R.color.gray_1)
            }
    }

}