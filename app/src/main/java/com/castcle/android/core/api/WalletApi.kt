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

package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.data.wallet.entity.*
import retrofit2.Response
import retrofit2.http.*

interface WalletApi {

    @POST("v2/wallets/{$PARAMETER_ID}/send/confirm")
    suspend fun confirmTransaction(
        @Path(PARAMETER_ID) id: String,
        @Body body: WalletTransactionRequest,
    ): Response<Unit>

    @POST("v2/wallets/{$PARAMETER_ACCOUNT_ID}/shortcuts/castcle")
    suspend fun createWalletShortcut(
        @Path(PARAMETER_ACCOUNT_ID) accountId: String,
        @Body body: CreateWalletShortcutRequest,
    ): Response<UserResponse>

    @DELETE("v2/wallets/{$PARAMETER_ACCOUNT_ID}/shortcuts/{$PARAMETER_SHORTCUT_ID}")
    suspend fun deleteWalletShortcut(
        @Path(PARAMETER_ACCOUNT_ID) accountId: String,
        @Path(PARAMETER_SHORTCUT_ID) shortcutId: String,
    ): Response<Unit>

    @GET("v2/qr-codes/castcle/{$PARAMETER_ID}")
    suspend fun getMyQrCode(
        @Path(PARAMETER_ID) id: String,
    ): Response<BaseResponse<String>>

    @GET("v2/wallets/{$PARAMETER_ID}/recent")
    suspend fun getRecentWalletAddress(
        @Path(PARAMETER_ID) id: String,
    ): Response<WalletRecentTransactionResponse>

    @GET("v2/wallets/{$PARAMETER_ID}/recent/search/by")
    suspend fun getWalletAddress(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_KEYWORD) keyword: String,
    ): Response<WalletRecentTransactionResponse>

    @GET("v2/wallets/{$PARAMETER_ID}")
    suspend fun getWalletBalance(
        @Path(PARAMETER_ID) id: String,
    ): Response<WalletBalanceResponse>

    @GET("v2/wallets/{$PARAMETER_ID}/history")
    suspend fun getWalletHistory(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_FILTER) filter: String,
    ): Response<BaseResponse<List<WalletHistoryResponse>>>

    @GET("v2/wallets/{$PARAMETER_ID}/shortcuts")
    suspend fun getWalletShortcuts(
        @Path(PARAMETER_ID) id: String,
    ): Response<WalletShortcutsResponse>

    @PUT("v2/wallets/{$PARAMETER_ACCOUNT_ID}/shortcuts/sort")
    suspend fun sortWalletShortcuts(
        @Path(PARAMETER_ACCOUNT_ID) accountId: String,
        @Body body: SortWalletShortcutRequest,
    ): Response<Unit>

    @POST("v2/wallets/{$PARAMETER_ID}/send/review")
    suspend fun reviewTransaction(
        @Path(PARAMETER_ID) id: String,
        @Body body: WalletTransactionRequest,
    ): Response<Unit>

}