package com.castcle.android.presentation.setting.register_email

import com.castcle.android.core.base.recyclerview.CastcleListener

interface RegisterEmailListener : CastcleListener {
    fun onRegisterEmailClicked(email: String)
}