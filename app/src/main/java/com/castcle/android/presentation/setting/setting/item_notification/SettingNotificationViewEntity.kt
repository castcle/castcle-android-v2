package com.castcle.android.presentation.setting.setting.item_notification

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity

data class SettingNotificationViewEntity(
    val item: NotificationBadgesEntity = NotificationBadgesEntity(),
    override val uniqueId: String = "${R.layout.item_setting_notification}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SettingNotificationViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SettingNotificationViewEntity>() == this
    }

    override fun viewType() = R.layout.item_setting_notification

}