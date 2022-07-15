package com.castcle.android.core.di

import android.content.Context
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan("com.castcle.android")
class ApplicationModule

val applicationModule = module {
    single {
        GoogleSignIn.getClient(
            get<Context>(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
    }
    single {
        LoginManager.getInstance()
    }
    single {
        TwitterAuthClient()
    }
}