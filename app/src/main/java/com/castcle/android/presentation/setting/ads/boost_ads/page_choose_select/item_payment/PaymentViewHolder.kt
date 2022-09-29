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

package com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_payment

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemPaymentBoostBinding
import com.castcle.android.domain.ads.type.PaymentType
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.PageChooseListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class PaymentViewHolder(
    private val binding: ItemPaymentBoostBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: PageChooseListener,
) : CastcleViewHolder<PaymentViewEntity>(binding.root) {

    override var item = PaymentViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onChoosePayment(item.paymentType)
        }
    }

    override fun bind(bindItem: PaymentViewEntity) {
        with(binding) {
            ivToken.loadImage(bindItem.paymentType.icon)
            tvObjectiveName.text = string(item.paymentType.name)

            tvBalanceValue.text = when (item.paymentType) {
                is PaymentType.TokenBalance -> {
                    bindMessageToken()
                    "${item.tokenBalance} CAST"
                }
                is PaymentType.CreditBalance -> {
                    bindMessageAdCredit()
                    "${item.adCreditBalance} CAST"
                }
            }
            tvStatusProcess.visibleOrInvisible(item.isSelected)
        }
    }

    private fun bindMessageToken() {
        binding.tvDetail.makeSpannableString(
            string(R.string.ad_boost_wallet_message), 38, 48
        )
    }

    private fun bindMessageAdCredit() {
        binding.tvDetail.makeSpannableString(
            string(R.string.ad_boost_ad_credit_message), 44, 54
        )
    }
}