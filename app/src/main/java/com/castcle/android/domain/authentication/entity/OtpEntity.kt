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