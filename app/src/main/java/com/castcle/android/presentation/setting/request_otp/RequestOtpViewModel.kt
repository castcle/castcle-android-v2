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

package com.castcle.android.presentation.setting.request_otp

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.request_otp.item_request_otp_email.RequestOtpEmailViewEntity
import com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile.RequestOtpMobileViewEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RequestOtpViewModel(
    private val database: CastcleDatabase,
    private val repository: AuthenticationRepository,
    private val type: OtpType,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<OtpEntity>()

    val views = MutableLiveData<CastcleViewEntity>()

    init {
        getItems()
    }

    private fun getItems() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { views.postValue(it) },
        ) {
            when (type) {
                is OtpType.Email, is OtpType.Password -> database.user().get(UserType.People)
                    .firstOrNull()
                    .let { RequestOtpEmailViewEntity(email = it?.email.orEmpty()) }
                is OtpType.Mobile -> RequestOtpMobileViewEntity()
            }
        }
    }

    fun requestOtp(countryCode: String, email: String, mobileNumber: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(it) },
        ) {
            val otp = OtpEntity(
                countryCode = countryCode,
                email = email,
                mobileNumber = mobileNumber,
                objective = when (type) {
                    is OtpType.Email, is OtpType.Password -> OtpObjective.ChangePassword
                    is OtpType.Mobile -> OtpObjective.VerifyMobile
                },
                type = type,
            )
            repository.requestOtp(otp = otp)
        }
    }

}