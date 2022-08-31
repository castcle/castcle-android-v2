package com.castcle.android.presentation.setting.request_otp.item_request_otp_mobile

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.metadata.entity.CountryCodeEntity

data class RequestOtpMobileViewEntity(
    var countryCode: CountryCodeEntity = CountryCodeEntity(),
    var mobileNumber: String = "",
    override val uniqueId: String = "${R.layout.item_request_otp_mobile}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<RequestOtpMobileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<RequestOtpMobileViewEntity>() == this
    }

    override fun viewType() = R.layout.item_request_otp_mobile

}