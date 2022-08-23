package com.castcle.android.presentation.setting.setting.item_logout

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class SettingLogoutViewEntity(
    override val uniqueId: String = "${R.layout.item_setting_logout}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingLogoutViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingLogoutViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_logout

}