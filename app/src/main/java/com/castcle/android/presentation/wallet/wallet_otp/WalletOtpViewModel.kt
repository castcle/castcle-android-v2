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

package com.castcle.android.presentation.wallet.wallet_otp

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.data.wallet.entity.WalletTransactionRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.presentation.wallet.wallet_otp.item_wallet_otp.WalletOtpViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletOtpViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val parameter: WalletOtpViewModelParameter,
    private val walletRepository: WalletRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<Unit>()

    val views = MutableStateFlow(
        WalletOtpViewEntity(
            otpEmail = parameter.otpEmail,
            otpMobile = parameter.otpMobile,
        )
    )

    fun confirmTransaction(otpEmail: OtpEntity, otpMobile: OtpEntity) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val body = parameter.request.copy(
                verification = WalletTransactionRequest.Verification(
                    email = otpEmail.toVerifyOtpRequest(),
                    mobile = otpMobile.toVerifyOtpRequest(),
                )
            )
            walletRepository.confirmTransaction(body)
        }
    }

    fun resendOtpEmail(otp: OtpEntity) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { views.emitOnSuspend(it) },
        ) {
            views.value.copy(otpEmail = authenticationRepository.requestOtp(otp))
        }
    }

    fun resendOtpMobile(otp: OtpEntity) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { views.emitOnSuspend(it) },
        ) {
            views.value.copy(otpMobile = authenticationRepository.requestOtp(otp))
        }
    }

    data class WalletOtpViewModelParameter(
        val otpEmail: OtpEntity = OtpEntity(),
        val otpMobile: OtpEntity = OtpEntity(),
        val request: WalletTransactionRequest = WalletTransactionRequest(),
    )

}