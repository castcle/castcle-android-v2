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

package com.castcle.android.presentation.wallet.wallet_transaction.item_wallet_transaction

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletTransactionBinding
import com.castcle.android.domain.wallet.type.WalletTransactionType
import com.castcle.android.presentation.wallet.wallet_transaction.WalletTransactionListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletTransactionViewHolder(
    private val binding: ItemWalletTransactionBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletTransactionListener,
) : CastcleViewHolder<WalletTransactionViewEntity>(binding.root) {

    override var item = WalletTransactionViewEntity()

    init {
        compositeDisposable += binding.clShortcut.onClick {
            listener.onCreateShortcutClicked(item.request.transaction?.address.orEmpty())
        }
        compositeDisposable += binding.tvConfirm.onClick {
            when (item.type) {
                is WalletTransactionType.Completed -> listener.onCloseClicked()
                is WalletTransactionType.Review -> listener.onConfirmClicked()
            }
        }
    }

    override fun bind(bindItem: WalletTransactionViewEntity) {
        binding.tvTransactionType.text = item.type.name
        binding.tvAmount1.text = item.request.transaction?.amount?.asCastToken().orEmpty()
        binding.tvAmount2.text = item.request.transaction?.amount?.asCastToken().orEmpty()
        binding.tvDate.text = item.request.detail?.timestamp?.toDateTime().orEmpty()
        binding.tvFrom.text = item.request.detail?.castcleId.orEmpty()
        binding.tvTo.text = item.request.detail?.targetCastcleId.orEmpty()
        binding.tvTo.text = item.request.detail?.targetCastcleId.orEmpty()
        binding.tvNote.text = item.request.transaction?.note.orEmpty()
        binding.groupNote.isGone = item.request.transaction?.note.isNullOrBlank()
        binding.tvWarning.isVisible =  item.type is WalletTransactionType.Review
        binding.clShortcut.isVisible = item.shortcutVisible
            && item.type is WalletTransactionType.Completed
        binding.tvConfirm.text = when (item.type) {
            is WalletTransactionType.Completed -> string(R.string.close)
            is WalletTransactionType.Review -> string(R.string.confirm)
        }
    }

}