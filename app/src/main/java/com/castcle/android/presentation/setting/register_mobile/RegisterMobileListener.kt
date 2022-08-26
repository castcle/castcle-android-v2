package com.castcle.android.presentation.setting.register_mobile

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.metadata.entity.CountryCodeEntity

interface RegisterMobileListener : CastcleListener {
    fun onConfirmClicked(countryCode: CountryCodeEntity, mobileNumber: String)
    fun onMobileCountryCodeClicked()
}