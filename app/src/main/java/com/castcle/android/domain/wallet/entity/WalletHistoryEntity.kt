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
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.TABLE_WALLET_HISTORY
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.wallet.entity.WalletHistoryResponse
import com.castcle.android.domain.wallet.type.WalletHistoryStatus
import com.castcle.android.domain.wallet.type.WalletHistoryType

@Entity(tableName = TABLE_WALLET_HISTORY)
data class WalletHistoryEntity(
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_createdAt", defaultValue = "0")
    val createdAt: Long = 0,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_id", defaultValue = "")
    @PrimaryKey
    val id: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_status", defaultValue = "")
    val status: WalletHistoryStatus = WalletHistoryStatus.Pending,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_type", defaultValue = "")
    val type: WalletHistoryType = WalletHistoryType.Send,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_value", defaultValue = "0")
    val value: Double = 0.0,
) {

    companion object {
        fun map(response: BaseResponse<List<WalletHistoryResponse>>?) =
            response?.payload.orEmpty().map {
                WalletHistoryEntity(
                    createdAt = it.createdAt?.toMilliSecond() ?: 0,
                    id = it.id.orEmpty(),
                    status = WalletHistoryStatus.getFromId(it.status),
                    type = WalletHistoryType.getFromId(it.type),
                    value = it.value ?: 0.0,
                )
            }
    }

}