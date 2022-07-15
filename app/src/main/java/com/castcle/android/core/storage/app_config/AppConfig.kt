package com.castcle.android.core.storage.app_config

import com.castcle.android.core.api.AuthenticationApi
import org.koin.core.annotation.Singleton

@Singleton
class AppConfig {

    var api: AuthenticationApi? = null

}