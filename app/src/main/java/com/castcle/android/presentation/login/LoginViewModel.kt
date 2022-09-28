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

package com.castcle.android.presentation.login

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.data.authentication.entity.LoginWithEmailRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.tracker.TrackerRepository
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.presentation.login.item_login.LoginViewEntity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.twitter.sdk.android.core.TwitterAuthToken
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val trackerRepository: TrackerRepository,
) : BaseViewModel() {

    val loginComplete = MutableLiveData<Unit>()

    val loginError = MutableLiveData<Throwable>()

    val items = MutableLiveData<List<CastcleViewEntity>>(listOf(LoginViewEntity()))

    init {
        logoutFacebook()
    }

    fun loginWithEmail(email: String, password: String) {
        launch(
            onError = { loginError.postValue(it) },
            onSuccess = { trackLogin(channel = "email", userId = it.first) }
        ) {
            authenticationRepository.loginWithEmail(LoginWithEmailRequest(email, password))
        }
    }

    fun loginWithFacebook() {
        launch(
            onError = {
                loginError.postValue(it)
                logoutFacebook()
            },
            onSuccess = { (userId, registered) ->
                if (registered) {
                    trackLogin(channel = "social_${SocialType.Facebook.id}", userId = userId)
                } else {
                    trackRegistration(channel = "social_${SocialType.Facebook.id}", userId = userId)
                }
                logoutFacebook()
            }
        ) {
            authenticationRepository.loginWithFacebook()
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        launch(
            onError = { loginError.postValue(it) },
            onSuccess = { (userId, registered) ->
                if (registered) {
                    trackLogin(channel = "social_${SocialType.Google.id}", userId = userId)
                } else {
                    trackRegistration(channel = "social_${SocialType.Google.id}", userId = userId)
                }
            }
        ) {
            authenticationRepository.loginWithGoogle(account)
        }
    }

    fun loginWithTwitter(token: TwitterAuthToken?) {
        launch(
            onError = { loginError.postValue(it) },
            onSuccess = { (userId, registered) ->
                if (registered) {
                    trackLogin(channel = "social_${SocialType.Twitter.id}", userId = userId)
                } else {
                    trackRegistration(channel = "social_${SocialType.Twitter.id}", userId = userId)
                }
            }
        ) {
            authenticationRepository.loginWithTwitter(token)
        }
    }

    fun logoutFacebook() {
        launch {
            authenticationRepository.loginOutFacebook()
        }
    }

    private fun trackLogin(channel: String, userId: String) {
        launch {
            loginComplete.postValue(Unit)
            trackerRepository.trackLogin(channel = channel, userId = userId)
        }
    }

    private fun trackRegistration(channel: String, userId: String) {
        launch {
            loginComplete.postValue(Unit)
            trackerRepository.trackRegistration(channel = channel, userId = userId)
        }
    }

}