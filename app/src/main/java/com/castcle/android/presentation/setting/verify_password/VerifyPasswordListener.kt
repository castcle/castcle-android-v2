package com.castcle.android.presentation.setting.verify_password

import com.castcle.android.core.base.recyclerview.CastcleListener

interface VerifyPasswordListener : CastcleListener {
    fun onVerifyPasswordClicked(password: String)
}