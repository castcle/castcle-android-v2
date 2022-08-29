package com.castcle.android.presentation.setting.change_password

import com.castcle.android.core.base.recyclerview.CastcleListener

interface ChangePasswordListener : CastcleListener {
    fun onChangePassword(password: String)
}