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

package com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit

import android.util.Base64
import com.bumptech.glide.Glide
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.copyToClipboard
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.toBitmap
import com.castcle.android.databinding.ItemWalletDepositBinding
import com.castcle.android.presentation.wallet.wallet_deposit.WalletDepositListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletDepositViewHolder(
    private val binding: ItemWalletDepositBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletDepositListener,
) : CastcleViewHolder<WalletDepositViewEntity>(binding.root) {

    override var item = WalletDepositViewEntity()

    init {
        compositeDisposable += binding.tvCastcleId.onClick {
            context().copyToClipboard(item.castcleId)
        }
        compositeDisposable += binding.ivSave.onClick {
            binding.shareWallet.toBitmap {
                listener.onSaveQrCodeWallet(it)
            }
        }
        compositeDisposable += binding.ivShare.onClick {
            binding.shareWallet.toBitmap {
                listener.onShareQrCodeWallet(it)
            }
        }
    }

    override fun bind(bindItem: WalletDepositViewEntity) {
        try {
            binding.tvCastcleId.text = item.castcleId
            binding.tvDescription.text = item.castcleId
            Glide.with(context())
                .load(Base64.decode(item.qrCode.toByteArray(), Base64.DEFAULT))
                .into(binding.ivQrCode)
            Glide.with(context())
                .load(Base64.decode(item.qrCode.toByteArray(), Base64.DEFAULT))
                .into(binding.ivQrCodeExport)
        } catch (exception: Exception) {
            binding.ivQrCode.setImageDrawable(null)
        }
    }

}