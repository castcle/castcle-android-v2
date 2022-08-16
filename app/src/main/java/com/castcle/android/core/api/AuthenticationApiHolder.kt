package com.castcle.android.core.api

import org.koin.core.annotation.Singleton

@Singleton
class AuthenticationApiHolder {
    var api: AuthenticationApi? = null
}