/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.data.authentication

import android.accounts.Account
import android.content.Context
import androidx.core.os.bundleOf
import androidx.room.withTransaction
import com.castcle.android.core.api.AuthenticationApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.core.extensions.*
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.authentication.entity.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.data.page.entity.SyncSocialRequest
import com.castcle.android.data.user.entity.*
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.domain.user.entity.*
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.sign_up.create_profile.entity.RegisterRequest
import com.facebook.*
import com.facebook.login.LoginManager
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Singleton
import timber.log.Timber
import kotlin.coroutines.*

@Singleton
class AuthenticationRepositoryImpl(
    private val api: AuthenticationApi,
    private val context: Context,
    private val database: CastcleDatabase,
    private val facebookLoginManager: LoginManager,
    private val glidePreloader: GlidePreloader,
) : AuthenticationRepository {

    override suspend fun changePassword(otp: OtpEntity) {
        apiCall { api.changePassword(body = otp.toChangePasswordRequest()) }
        database.user().get(UserType.People).firstOrNull()
            ?.copy(passwordNotSet = false)
            ?.also { database.user().update(it) }
    }

    override suspend fun getAccessToken(): AccessTokenEntity {
        return database.accessToken().get() ?: AccessTokenEntity()
    }

    override suspend fun getFacebookPageProfile(): List<SyncSocialRequest> {
        return suspendCoroutine { coroutine ->
            GraphRequest(
                accessToken = AccessToken.getCurrentAccessToken(),
                graphPath = "me/accounts",
                httpMethod = HttpMethod.GET,
                parameters = bundleOf("fields" to "about,access_token,cover,id,link,name,picture.type(large),username"),
                callback = {
                    val type = object : TypeToken<GetFacebookPageProfileResponse>() {}.type
                    val response =
                        Gson().fromJson<GetFacebookPageProfileResponse>(it.rawResponse, type)
                    val pageItems = response.data.orEmpty().map { map ->
                        SyncSocialRequest(
                            authToken = map.access_token,
                            avatar = map.picture?.data?.url,
                            cover = map.cover?.source,
                            displayName = map.name,
                            link = map.link,
                            overview = map.about,
                            provider = SocialType.Facebook.id,
                            socialId = map.id,
                            userName = map.username,
                        )
                    }
                    coroutine.resume(pageItems)
                }
            ).executeAsync()
        }
    }

    override suspend fun getFacebookUserProfile(): LoginWithSocialRequest {
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

    override suspend fun loginWithEmail(body: LoginWithEmailRequest): Pair<String, Boolean> {
        return updateWhenLoginSuccess(apiCall { api.loginWithEmail(body = body) })
    }

    override suspend fun loginWithFacebook(): Pair<String, Boolean> {
        return loginWithSocial(getFacebookUserProfile())
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loginWithGoogle(signInAccount: GoogleSignInAccount): Pair<String, Boolean> {
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
        return loginWithSocial(body)
    }

    override suspend fun loginWithSocial(body: LoginWithSocialRequest): Pair<String, Boolean> {
        return updateWhenLoginSuccess(apiCall { api.loginWithSocial(body = body) })
    }

    override suspend fun loginWithTwitter(token: TwitterAuthToken?): Pair<String, Boolean> {
        return loginWithSocial(getTwitterUserProfile(token))
    }

    override suspend fun loginOut() {
        database.withTransaction {
            fetchGuestAccessToken()
            loginOutFacebook()
            database.cast().delete()
            database.linkSocial().delete()
            database.notificationBadges().delete()
            database.profile().delete()
            database.recursiveRefreshToken().delete()
            database.syncSocial().delete()
            database.user().delete()
        }
    }

    override suspend fun loginOutFacebook() {
        return suspendCoroutine { coroutine ->
            GraphRequest(
                accessToken = AccessToken.getCurrentAccessToken(),
                graphPath = "/me/permissions",
                httpMethod = HttpMethod.DELETE,
                callback = {
                    facebookLoginManager.logOut()
                    coroutine.resume(Unit)
                }
            ).executeAsync()
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

    override suspend fun requestOtp(otp: OtpEntity): OtpEntity {
        val response = when (otp.type) {
            is OtpType.Email -> apiCall { api.requestOtpEmail(body = otp.toRequestOtpRequest()) }
            is OtpType.Mobile -> apiCall { api.requestOtpMobile(body = otp.toRequestOtpRequest()) }
            else -> null
        }
        return otp.fromRequestOtpResponse(response)
    }

    override suspend fun resentVerifyEmail() {
        apiCall { api.resentVerifyEmail() }
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

    private suspend fun updateWhenLoginSuccess(response: LoginResponse?): Pair<String, Boolean> {
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
        return user.id to (response?.registered ?: false)
    }

    override suspend fun updateMobileNumber(otp: OtpEntity): OtpEntity {
        val updatedOtp = verifyOtp(otp)
        val mobileNumber = if (otp.mobileNumber.startsWith("0")) {
            otp.mobileNumber.drop(1)
        } else {
            otp.mobileNumber
        }
        apiCall { api.updateMobileNumber(body = updatedOtp.toUpdateMobileNumberRequest()) }
        database.user().get(UserType.People).firstOrNull()
            ?.copy(mobileCountryCode = otp.countryCode, mobileNumber = mobileNumber)
            ?.also { database.user().update(it) }
        return updatedOtp
    }

    override suspend fun verifyOtp(otp: OtpEntity): OtpEntity {
        val response = when (otp.type) {
            OtpType.Email -> apiCall { api.verifyOtpEmail(body = otp.toVerifyOtpRequest()) }
            OtpType.Mobile -> apiCall { api.verifyOtpMobile(body = otp.toVerifyOtpRequest()) }
            OtpType.Password -> apiCall { api.verifyPassword(body = otp.toVerifyOtpRequest()) }
        }
        return otp.fromVerifyOtpResponse(response)
    }

    override suspend fun emailIsExist(emailIsExistRequest: EmailIsExistRequest):
        Flow<BaseUiState<AuthExistResponse>> {
        return toApiCallFlow { api.checkEmailIsExists(emailIsExistRequest) }
    }

    override suspend fun castcleIdIsExist(castcleRequest: CastcleIdExistRequest):
        Flow<BaseUiState<AuthExistResponse>> {
        return toApiCallFlow { api.checkCastcleIdExists(castcleRequest) }
    }

    override suspend fun getSuggestionCastcleId(displayNameRequest: DisplayNameRequest):
        Flow<BaseUiState<AuthPayload>> {
        return toApiCallFlow { api.getSuggestCastcleId(displayNameRequest) }
    }

    override suspend fun registerWithEmail(reqisterRequest: RegisterRequest): Flow<BaseUiState<UserEntity>> {
        return flow {
            emit(BaseUiState.Loading(null, true))
            apiCall {
                api.registerWithEmail(reqisterRequest).also {
                    emit(BaseUiState.Loading(null, false))
                    if (it.isSuccessful) {
                        updateWhenLoginSuccess(it.body())
                        emit(BaseUiState.Success(_data = UserEntity.mapOwner(it.body()?.profile)))
                    } else {
                        emit(BaseUiState.Error(ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }.distinctUntilChanged()
    }

    override suspend fun createPage(reqisterRequest: RegisterRequest): Flow<BaseUiState<UserEntity>> {
        return flow {
            apiCall {
                emit(BaseUiState.Loading(null, true))
                api.createPage(reqisterRequest).also {
                    emit(BaseUiState.Loading(null, false))
                    if (it.isSuccessful) {
                        updateCreatePageSuccess(it.body())
                        emit(BaseUiState.Success(_data = UserEntity.mapOwner(it.body())))
                    } else {
                        emit(BaseUiState.Error(ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }
    }

    private suspend fun updateCreatePageSuccess(response: UserResponse?) {
        val page = UserEntity.mapOwner(response)
        val linkSocial = LinkSocialEntity.map(response)
        val syncSocialUser = SyncSocialEntity.map(response)
        val syncSocialPage = SyncSocialEntity.map(response)
        glidePreloader.loadUser(page)
        database.withTransaction {
            database.linkSocial().insert(linkSocial)
            database.syncSocial().insert(syncSocialPage.plus(syncSocialUser))
            database.user().upsert(page)
        }
    }
}
