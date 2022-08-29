package com.castcle.android.presentation.login

import com.castcle.android.core.base.recyclerview.CastcleListener

interface LoginListener : CastcleListener {
    fun onEmailLoginClicked(email: String, password: String)
    fun onFacebookLoginClicked()
    fun onForgotPasswordClicked()
    fun onGoogleLoginClicked()
    fun onTwitterLoginClicked()
    fun onUrlClicked(url: String)
}