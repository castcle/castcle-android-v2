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

package com.castcle.android.domain.wallet.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_DASHBOARD
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

@Entity(tableName = TABLE_WALLET_DASHBOARD)
data class WalletDashboardEntity(
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_createAt", defaultValue = "0")
    val createAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_filter", defaultValue = "")
    val filter: WalletHistoryFilter = WalletHistoryFilter.WalletBalance,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_id", defaultValue = "0")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_relationId", defaultValue = "")
    val relationId: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_type", defaultValue = "")
    val type: WalletDashboardType = WalletDashboardType.Balance,
)