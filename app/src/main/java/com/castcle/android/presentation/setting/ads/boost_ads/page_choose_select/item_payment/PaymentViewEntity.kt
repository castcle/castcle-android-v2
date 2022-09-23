package com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_payment

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.ads.type.PaymentType

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 19/9/2022 AD at 10:44.

class PaymentViewEntity(
    val tokenBalance: Double = 0.0,
    val adCreditBalance: Double = 0.0,
    val isSelected: Boolean = false,
    val paymentType: PaymentType = PaymentType.TokenBalance,
    override val uniqueId: String = "${R.layout.item_payment_boost}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<PaymentViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<PaymentViewEntity>() == this
    }

    override fun viewType() = R.layout.item_payment_boost
}
