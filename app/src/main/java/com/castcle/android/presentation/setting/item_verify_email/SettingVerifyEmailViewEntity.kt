package com.castcle.android.presentation.setting.item_verify_email

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class SettingVerifyEmailViewEntity(
    override val uniqueId: String = "${R.layout.item_setting_verify_email}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingVerifyEmailViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingVerifyEmailViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_verify_email

}