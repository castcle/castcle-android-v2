package com.castcle.android.presentation.setting.country_code

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.metadata.entity.CountryCodeEntity

interface CountryCodeListener : CastcleListener {
    fun onCountryCodeClicked(countryCode: CountryCodeEntity)
}