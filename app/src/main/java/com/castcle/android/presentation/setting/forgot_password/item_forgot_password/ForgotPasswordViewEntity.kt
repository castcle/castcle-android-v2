package com.castcle.android.presentation.setting.forgot_password.item_forgot_password

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ForgotPasswordViewEntity(
    var email: String = "",
    override val uniqueId: String = "${R.layout.item_forgot_password}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ForgotPasswordViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ForgotPasswordViewEntity>() == this
    }

    override fun viewType() = R.layout.item_forgot_password

}