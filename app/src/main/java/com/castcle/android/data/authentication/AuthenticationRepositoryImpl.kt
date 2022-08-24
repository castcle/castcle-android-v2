package com.castcle.android.data.authentication

import android.accounts.Account
import android.content.Context
import androidx.core.os.bundleOf
import androidx.room.withTransaction
import com.castcle.android.core.api.AuthenticationApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.*
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.authentication.entity.*
import com.castcle.android.data.user.entity.GetFacebookUserProfileResponse
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.SocialType
import com.facebook.*
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import org.koin.core.annotation.Singleton
import timber.log.Timber
import kotlin.coroutines.*

@Singleton
class AuthenticationRepositoryImpl(
    private val api: AuthenticationApi,
    private val database: CastcleDatabase,
    private val context: Context,
    private val glidePreloader: GlidePreloader,
) : AuthenticationRepository {

    override suspend fun getAccessToken(): AccessTokenEntity {
        return database.accessToken().get() ?: AccessTokenEntity()
    }

    private suspend fun getFacebookUserProfile(): LoginWithSocialRequest {
        return suspendCoroutine { coroutine ->
            GraphRequest(
                accessToken = AccessToken.getCurrentAccessToken(),
                graphPath = "me",
                httpMethod = HttpMethod.GET,
                parameters = bundleOf("fields" to "email,id,name,picture.type(large)"),
                callback = {
                    val type = object : TypeToken<GetFacebookUserProfileResponse>() {}.type
                    val response =
                        Gson().fromJson<GetFacebookUserProfileResponse>(it.rawResponse, type)
                    val body = LoginWithSocialRequest(
                        authToken = AccessToken.getCurrentAccessToken()?.token,
                        avatar = response.picture?.data?.url,
                        displayName = response.name,
                        email = response.email,
                        provider = SocialType.Facebook.id,
                        socialId = response.id,
                    )
                    coroutine.resume(body)
                }
            ).executeAsync()
        }
    }

    override suspend fun getFirebaseMessagingToken(): String {
        return suspendCoroutine { coroutine ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    coroutine.resume(task.result)
                } else {
                    coroutine.resume("")
                }
            }
        }
    }

    private suspend fun getTwitterUserProfile(token: TwitterAuthToken?): LoginWithSocialRequest {
        return suspendCoroutine { coroutine ->
            TwitterCore.getInstance().apiClient.accountService
                .verifyCredentials(false, true, true)
                .enqueue(object : Callback<User>() {
                    override fun failure(exception: TwitterException?) {
                        coroutine.resumeWithException(Throwable(exception?.message))
                    }

                    override fun success(result: Result<User>?) {
                        val body = LoginWithSocialRequest(
                            authToken = "${token?.token}|${token?.secret}",
                            avatar = result?.data?.getLargeProfileImageUrlHttps(),
                            cover = result?.data?.profileBannerUrl,
                            displayName = result?.data?.name,
                            email = result?.data?.email,
                            link = result?.data?.idStr?.let { "https://twitter.com/i/user/$it" },
                            overview = result?.data?.description,
                            provider = SocialType.Twitter.id,
                            socialId = result?.data?.idStr,
                        )
                        coroutine.resume(body)
                    }
                })
        }
    }

    override suspend fun fetchGuestAccessToken() {
        val body = GetGuestAccessTokenRequest(context.getDeviceUniqueId())
        val response = apiCall { api.getGuestAccessToken(body = body) }
        database.accessToken().insert(AccessTokenEntity.map(response))
    }

    override suspend fun linkWithFacebook() {
        linkWithSocial(getFacebookUserProfile())
    }

    override suspend fun linkWithSocial(body: LoginWithSocialRequest) {
        updateWhenLoginSuccess(apiCall { api.linkWithSocial(body = body) })
    }

    override suspend fun linkWithTwitter(token: TwitterAuthToken?) {
        linkWithSocial(getTwitterUserProfile(token))
    }

    override suspend fun loginWithEmail(body: LoginWithEmailRequest) {
        updateWhenLoginSuccess(apiCall { api.loginWithEmail(body = body) })
    }

    override suspend fun loginWithFacebook() {
        loginWithSocial(getFacebookUserProfile())
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loginWithGoogle(signInAccount: GoogleSignInAccount) {
        val body = suspendCoroutine<LoginWithSocialRequest> { coroutine ->
            val plusScope = "oauth2:https://www.googleapis.com/auth/plus.me"
            val userInfoScope = "https://www.googleapis.com/auth/userinfo.profile"
            val account = signInAccount.account ?: Account("", "")
            val token = GoogleAuthUtil.getToken(context, account, "$plusScope $userInfoScope")
            val body = LoginWithSocialRequest(
                authToken = token,
                avatar = signInAccount.photoUrl?.toString(),
                displayName = account.name,
                email = signInAccount.email,
                provider = SocialType.Google.id,
                socialId = signInAccount.id,
            )
            coroutine.resume(body)
        }
        loginWithSocial(body)
    }

    override suspend fun loginWithSocial(body: LoginWithSocialRequest) {
        updateWhenLoginSuccess(apiCall { api.loginWithSocial(body = body) })
    }

    override suspend fun loginWithTwitter(token: TwitterAuthToken?) {
        loginWithSocial(getTwitterUserProfile(token))
    }

    override suspend fun loginOut() {
        database.withTransaction {
            fetchGuestAccessToken()
            database.cast().delete()
            database.linkSocial().delete()
            database.notificationBadges().delete()
            database.profile().delete()
            database.recursiveRefreshToken().delete()
            database.syncSocial().delete()
            database.user().delete()
        }
    }

    override suspend fun registerFirebaseMessagingToken() {
        try {
            val body = RegisterFirebaseMessagingTokenRequest(
                firebaseToken = getFirebaseMessagingToken(),
                uuid = context.getDeviceUniqueId(),
            )
            api.registerFirebaseMessagingToken(body = body)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    override suspend fun unregisterFirebaseMessagingToken() {
        try {
            val body = RegisterFirebaseMessagingTokenRequest(
                firebaseToken = getFirebaseMessagingToken(),
                uuid = context.getDeviceUniqueId(),
            )
            api.unregisterFirebaseMessagingToken(body = body)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    private suspend fun updateWhenLoginSuccess(response: LoginResponse?) {
        val user = UserEntity.mapOwner(response?.profile)
        val page = UserEntity.mapOwner(response?.pages)
        val linkSocial = LinkSocialEntity.map(response?.profile)
        val syncSocialUser = SyncSocialEntity.map(response?.profile)
        val syncSocialPage = SyncSocialEntity.map(response?.pages)
        val accessToken = AccessTokenEntity.map(response)
        glidePreloader.loadUser(page.plus(user))
        database.withTransaction {
            database.linkSocial().delete()
            database.linkSocial().insert(linkSocial)
            database.syncSocial().delete()
            database.syncSocial().insert(syncSocialPage.plus(syncSocialUser))
            database.user().upsert(page.plus(user))
            database.accessToken().insert(accessToken)
        }
        registerFirebaseMessagingToken()
    }

}