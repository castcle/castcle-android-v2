package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.PARAMETER_FILTER
import com.castcle.android.core.constants.PARAMETER_ID
import com.castcle.android.data.wallet.entity.WalletBalanceResponse
import com.castcle.android.data.wallet.entity.WalletHistoryResponse
import retrofit2.Response
import retrofit2.http.*

interface WalletApi {

    @GET("v2/qr-codes/castcle/{$PARAMETER_ID}")
    suspend fun getMyQrCode(
        @Path(PARAMETER_ID) id: String,
    ): Response<BaseResponse<String>>

    @GET("v2/wallets/{$PARAMETER_ID}")
    suspend fun getWalletBalance(
        @Path(PARAMETER_ID) id: String,
    ): Response<WalletBalanceResponse>

    @GET("v2/wallets/{$PARAMETER_ID}/history")
    suspend fun getWalletHistory(
        @Path(PARAMETER_ID) id: String,
        @Query(PARAMETER_FILTER) filter: String,
    ): Response<BaseResponse<List<WalletHistoryResponse>>>

}