package com.castcle.android.presentation.setting

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity

interface SettingListener : CastcleListener {
    fun onAccountClick()
    fun onLogoutClick()
    fun onNewPageClick()
    fun onNotificationClicked()
    fun onUrlClicked(url: String)
    fun onUserClicked(user: UserEntity)
    fun onVerifyEmailClicked()
}