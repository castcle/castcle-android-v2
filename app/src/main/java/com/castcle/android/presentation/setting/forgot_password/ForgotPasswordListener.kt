package com.castcle.android.presentation.setting.forgot_password

import com.castcle.android.core.base.recyclerview.CastcleListener

interface ForgotPasswordListener : CastcleListener {
    fun onForgotPasswordClicked(email: String)
}