package com.castcle.android.presentation.setting.setting.item_profile

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserWithSyncSocialEntity

data class SettingProfileViewEntity(
    override val uniqueId: String = "",
    val userWithSocial: UserWithSyncSocialEntity = UserWithSyncSocialEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingProfileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingProfileViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_profile

}