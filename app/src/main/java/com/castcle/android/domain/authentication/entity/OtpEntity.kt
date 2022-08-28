package com.castcle.android.domain.authentication.entity

import android.os.Parcelable
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.authentication.entity.*
import com.castcle.android.domain.authentication.type.OtpObjective
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtpEntity(
    val countryCode: String = "",
    val expiresTime: Long = 0L,
    val mobileNumber: String = "",
    val objective: OtpObjective = OtpObjective.VerifyMobile,
    val otp: String = "",
    val refCode: String = "",
) : Parcelable {

    fun toRequestOtpMobile() = RequestOtpMobileRequest(
        countryCode = countryCode,
        mobileNumber = mobileNumber,
        objective = objective.id,
    )

    fun toUpdateMobileNumber() = UpdateMobileNumberRequest(
        countryCode = countryCode,
        mobileNumber = mobileNumber,
        objective = objective.id,
        refCode = refCode,
    )

    fun toVerifyOtpMobile() = VerifyOtpMobileRequest(
        countryCode = countryCode,
        mobileNumber = mobileNumber,
        objective = objective.id,
        otp = otp,
        refCode = refCode,
    )

    fun fromRequestOtpMobile(response: RequestOtpMobileResponse?) = copy(
        expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
        refCode = response?.refCode.orEmpty(),
    )

    fun fromVerifyOtpMobile(response: VerifyOtpMobileResponse?) = copy(
        expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
        refCode = response?.refCode.orEmpty(),
    )

    companion object {
        fun map(
            request: RequestOtpMobileRequest,
            response: RequestOtpMobileResponse? = null,
        ) = OtpEntity(
            countryCode = request.countryCode.orEmpty(),
            expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
            mobileNumber = request.mobileNumber.orEmpty(),
            objective = OtpObjective.getFromId(request.objective),
            refCode = response?.refCode.orEmpty(),
        )
    }

}