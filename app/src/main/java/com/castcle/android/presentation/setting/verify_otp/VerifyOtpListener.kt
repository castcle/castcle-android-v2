package com.castcle.android.presentation.setting.verify_otp

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.authentication.entity.OtpEntity

interface VerifyOtpListener : CastcleListener {
    fun onResentOtpClicked(otp: OtpEntity)
    fun onVerifyOtp(otp: OtpEntity)
}