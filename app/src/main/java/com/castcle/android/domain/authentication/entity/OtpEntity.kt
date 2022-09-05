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

package com.castcle.android.domain.authentication.entity

import android.os.Parcelable
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.authentication.entity.*
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtpEntity(
    val accessToken: String = "",
    val countryCode: String = "",
    val email: String = "",
    val expiresTime: Long = 0L,
    val mobileNumber: String = "",
    val password: String = "",
    val objective: OtpObjective = OtpObjective.VerifyMobile,
    val otp: String = "",
    val refCode: String = "",
    val type: OtpType = OtpType.Email,
) : Parcelable {

    fun toChangePasswordRequest() = ChangePasswordRequest(
        email = email,
        newPassword = password,
        objective = objective.id,
        refCode = refCode,
    )

    fun toRequestOtpRequest() = RequestOtpRequest(
        countryCode = countryCode.ifBlank { null },
        email = email.ifBlank { null },
        mobileNumber = mobileNumber.ifBlank { null },
        objective = objective.id,
    )

    fun toUpdateMobileNumberRequest() = UpdateMobileNumberRequest(
        countryCode = countryCode,
        mobileNumber = mobileNumber,
        objective = objective.id,
        refCode = refCode,
    )

    fun toVerifyOtpRequest() = VerifyOtpRequest(
        countryCode = countryCode.ifBlank { null },
        email = email.ifBlank { null },
        mobileNumber = mobileNumber.ifBlank { null },
        objective = objective.id,
        otp = otp,
        password = password.ifBlank { null },
        refCode = refCode,
    )

    fun fromRequestOtpResponse(response: RequestOtpResponse?) = copy(
        expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
        refCode = response?.refCode.orEmpty(),
    )

    fun fromVerifyOtpResponse(response: VerifyOtpResponse?) = copy(
        accessToken = response?.accessToken.orEmpty(),
        expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
        refCode = response?.refCode.orEmpty(),
    )

}