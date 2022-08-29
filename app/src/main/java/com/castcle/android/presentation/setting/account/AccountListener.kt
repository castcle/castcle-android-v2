package com.castcle.android.presentation.setting.account

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.authentication.type.OtpType

interface AccountListener : CastcleListener {
    fun onChangePasswordClicked()
    fun onDeleteAccountClicked()
    fun onLinkFacebookClicked()
    fun onLinkTwitterClicked()
    fun onRequestOtpClicked(type: OtpType)
    fun onResentVerifyEmailClicked()
}