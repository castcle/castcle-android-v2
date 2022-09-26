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

package com.castcle.android.presentation.setting.create_page_option

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.data.page.entity.CreatePageWithSocialRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.page.PageRepository
import com.castcle.android.domain.user.UserRepository
import com.twitter.sdk.android.core.TwitterAuthToken
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreatePageOptionViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val pageRepository: PageRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onFacebookPageProfileResult = MutableSharedFlow<List<CreatePageWithSocialRequest>>()

    val onSuccess = MutableSharedFlow<Unit>()

    init {
        logoutFacebook()
    }

    fun createPageWithFacebook(body: CreatePageWithSocialRequest) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { updateCurrentUser() },
        ) {
            pageRepository.createPageWithFacebook(body = body)
            authenticationRepository.loginOutFacebook()
        }
    }

    fun createPageWithTwitter(token: TwitterAuthToken?) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { updateCurrentUser() },
        ) {
            pageRepository.createPageWithTwitter(token = token)
        }
    }

    fun getFacebookPageProfile() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onFacebookPageProfileResult.emitOnSuspend(it) },
        ) {
            authenticationRepository.getFacebookPageProfile()
        }
    }

    fun logoutFacebook() {
        launch {
            authenticationRepository.loginOutFacebook()
        }
    }

    private fun updateCurrentUser() {
        launch(
            onError = { onSuccess.emitOnSuspend() },
            onSuccess = { onSuccess.emitOnSuspend() },
        ) {
            userRepository.fetchUserPage()
            userRepository.fetchUserProfile()
        }
    }

}