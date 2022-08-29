package com.castcle.android.presentation.setting.request_otp

import com.castcle.android.core.base.recyclerview.CastcleListener

interface RequestOtpListener : CastcleListener {
    fun onMobileCountryCodeClicked()
    fun onRequestOtp(countryCode: String, email: String, mobileNumber: String)
}