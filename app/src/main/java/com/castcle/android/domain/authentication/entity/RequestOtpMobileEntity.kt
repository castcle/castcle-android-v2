package com.castcle.android.domain.authentication.entity

import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.authentication.entity.RequestOtpMobileRequest
import com.castcle.android.data.authentication.entity.RequestOtpMobileResponse

data class RequestOtpMobileEntity(
    val countryCode: String = "",
    val expiresTime: Long = 0L,
    val mobileNumber: String = "",
    val objective: String = "",
    val refCode: String = "",
) {

    companion object {
        fun map(
            request: RequestOtpMobileRequest,
            response: RequestOtpMobileResponse? = null,
        ) = RequestOtpMobileEntity(
            countryCode = request.countryCode.orEmpty(),
            expiresTime = response?.expiresTime?.toMilliSecond() ?: 0L,
            mobileNumber = request.mobileNumber.orEmpty(),
            objective = request.objective.orEmpty(),
            refCode = response?.refCode.orEmpty(),
        )
    }

}