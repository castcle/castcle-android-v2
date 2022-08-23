package com.castcle.android.presentation.setting.setting.item_profile_section

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.setting.setting.item_profile.SettingProfileViewEntity

data class SettingProfileSectionViewEntity(
    val items: List<SettingProfileViewEntity> = listOf(),
    override val uniqueId: String = "${R.layout.item_setting_profile_section}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingProfileSectionViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingProfileSectionViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_profile_section

}