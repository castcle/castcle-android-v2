package com.castcle.android.presentation.notification

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.presentation.dialog.option.OptionDialogType

interface NotificationListener : CastcleListener {
    fun onCastNotificationClicked(castId: String, notificationId: String)
    fun onFollowNotificationClicked(notificationId: String, userId: String)
    fun onOptionClicked(type: OptionDialogType)
}