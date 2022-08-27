package com.castcle.android.domain.authentication

import com.castcle.android.data.authentication.entity.*
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.authentication.entity.RequestOtpMobileEntity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.twitter.sdk.android.core.TwitterAuthToken

interface AuthenticationRepository {
    suspend fun getAccessToken(): AccessTokenEntity
    suspend fun getFirebaseMessagingToken(): String
    suspend fun fetchGuestAccessToken()
    suspend fun linkWithFacebook()
    suspend fun linkWithSocial(body: LoginWithSocialRequest)
    suspend fun linkWithTwitter(token: TwitterAuthToken?)
    suspend fun loginWithEmail(body: LoginWithEmailRequest)
    suspend fun loginWithFacebook()
    suspend fun loginWithGoogle(signInAccount: GoogleSignInAccount)
    suspend fun loginWithSocial(body: LoginWithSocialRequest)
    suspend fun loginWithTwitter(token: TwitterAuthToken?)
    suspend fun loginOut()
    suspend fun registerFirebaseMessagingToken()
    suspend fun requestOtpMobile(body: RequestOtpMobileRequest): RequestOtpMobileEntity
    suspend fun resentVerifyEmail()
    suspend fun unregisterFirebaseMessagingToken()
}