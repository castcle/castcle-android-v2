package com.castcle.android.presentation.setting.verify_otp.item_verify_otp

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.authentication.entity.OtpEntity

data class VerifyOtpViewEntity(
    val otp: OtpEntity = OtpEntity(),
    var otpNumber: String = "",
    override val uniqueId: String = "${R.layout.item_verify_otp}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<VerifyOtpViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<VerifyOtpViewEntity>() == this
    }

    override fun viewType() = R.layout.item_verify_otp

}