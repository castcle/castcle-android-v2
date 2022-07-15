package com.castcle.android.presentation.setting.item_add_page

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class SettingAddPageViewEntity(
    override val uniqueId: String = "${R.layout.item_setting_add_page}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingAddPageViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingAddPageViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_add_page

}