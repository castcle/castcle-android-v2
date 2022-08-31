package com.castcle.android.presentation.setting.request_otp.item_request_otp_email

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class RequestOtpEmailViewEntity(
    val email: String = "",
    override val uniqueId: String = "${R.layout.item_request_otp_email}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<RequestOtpEmailViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<RequestOtpEmailViewEntity>() == this
    }

    override fun viewType() = R.layout.item_request_otp_email

}