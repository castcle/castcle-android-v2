package com.castcle.android.presentation.setting.item_menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.setting.SettingListener

data class SettingMenuViewEntity(
    val action: (SettingListener) -> Unit = {},
    @StringRes val menu: Int = R.string.account,
    @DrawableRes val menuIcon: Int = R.drawable.ic_user,
    override val uniqueId: String = "",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingMenuViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingMenuViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_menu

}