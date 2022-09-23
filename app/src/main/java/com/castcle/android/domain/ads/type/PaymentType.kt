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

package com.castcle.android.domain.ads.type

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.TypeConverter
import com.castcle.android.R
import kotlinx.parcelize.Parcelize

sealed class PaymentType(
    val id: String,

    @DrawableRes
    val icon: Int = 0,

    @StringRes
    val name: Int = 0
) : Parcelable {

    @Parcelize
    object TokenBalance : PaymentType(
        id = "token-wallet",
        icon = R.drawable.ic_wallet,
        name = R.string.token_wallet
    )

    @Parcelize
    object CreditBalance : PaymentType(
        id = "ads-credit",
        icon = R.drawable.ic_ad_credit,
        name = R.string.ads_credit
    )

    companion object {
        fun getFromId(id: String?) = when (id) {
            TokenBalance.id -> TokenBalance
            CreditBalance.id -> CreditBalance
            else -> TokenBalance
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: PaymentType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}