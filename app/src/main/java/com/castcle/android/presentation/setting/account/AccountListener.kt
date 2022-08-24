package com.castcle.android.presentation.setting.account

import com.castcle.android.core.base.recyclerview.CastcleListener

interface AccountListener : CastcleListener {
    fun onDeleteAccountClicked()
    fun onLinkFacebookClicked()
    fun onLinkTwitterClicked()
    fun onMobileNumberClicked()
    fun onPasswordClicked()
    fun onResentVerifyEmailClicked()
}