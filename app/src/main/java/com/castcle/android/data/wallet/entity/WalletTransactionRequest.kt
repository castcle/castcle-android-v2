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

package com.castcle.android.data.wallet.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.castcle.android.data.authentication.entity.VerifyOtpRequest
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class WalletTransactionRequest(
    @SerializedName("detail") val detail: Detail? = null,
    @SerializedName("transaction") val transaction: Transaction? = null,
    @SerializedName("verification") val verification: Verification? = null,
) : Parcelable {

    @Parcelize
    data class Detail(
        @SerializedName("castcleId") val castcleId: String? = null,
        @SerializedName("targetCastcleId") val targetCastcleId: String? = null,
        @SerializedName("timestamp") val timestamp: Long? = null,
        @SerializedName("userId") val userId: String? = null,
    ) : Parcelable

    @Parcelize
    data class Transaction(
        @SerializedName("address") val address: String? = null,
        @SerializedName("amount") val amount: Double? = null,
        @SerializedName("chainId") val chainId: String? = null,
        @SerializedName("memo") val memo: String? = null,
        @SerializedName("note") val note: String? = null,
    ) : Parcelable

    @Parcelize
    data class Verification(
        @SerializedName("email") val email: VerifyOtpRequest? = null,
        @SerializedName("mobile") val mobile: VerifyOtpRequest? = null,
    ) : Parcelable

}