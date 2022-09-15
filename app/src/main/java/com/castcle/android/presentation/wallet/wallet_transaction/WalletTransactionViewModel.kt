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

package com.castcle.android.presentation.wallet.wallet_transaction

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.wallet.entity.CreateWalletShortcutRequest
import com.castcle.android.data.wallet.entity.WalletTransactionRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.domain.wallet.WalletRepository
import com.castcle.android.domain.wallet.type.WalletTransactionType
import com.castcle.android.presentation.wallet.wallet_transaction.item_wallet_transaction.WalletTransactionViewEntity
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WalletTransactionViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val request: WalletTransactionRequest = WalletTransactionRequest(),
    private val type: WalletTransactionType = WalletTransactionType.Review,
    private val walletRepository: WalletRepository,
) : BaseViewModel() {

    val onCreateShortcutSuccess = MutableSharedFlow<Unit>()

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<Pair<OtpEntity, OtpEntity>>()

    private val targetUserId = request.transaction?.address

    val views = database.walletShortcut().retrieve()
        .map { it.find { find -> find.user?.id == targetUserId } == null }
        .map { it && database.user().get().find { find -> find.id == targetUserId } == null }
        .map { shortcutVisible -> WalletTransactionViewEntity(request, shortcutVisible, type) }
        .distinctUntilChanged()

    fun createWalletShortcut(userId: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onCreateShortcutSuccess.emitOnSuspend(it) },
        ) {
            walletRepository.createWalletShortcut(CreateWalletShortcutRequest(userId = userId))
        }
    }

    fun requestOtp() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val user = database.user().get(UserType.People).firstOrNull()
            val otpEmail = OtpEntity(
                email = user?.email.orEmpty(),
                objective = OtpObjective.SendToken,
                type = OtpType.Email,
            )
            val otpMobile = OtpEntity(
                countryCode = user?.mobileCountryCode.orEmpty(),
                mobileNumber = user?.mobileNumber.orEmpty(),
                objective = OtpObjective.SendToken,
                type = OtpType.Mobile,
            )
            authenticationRepository.requestOtp(otpEmail) to
                authenticationRepository.requestOtp(otpMobile)
        }
    }

}