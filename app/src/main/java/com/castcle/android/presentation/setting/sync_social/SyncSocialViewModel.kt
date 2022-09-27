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

package com.castcle.android.presentation.setting.sync_social

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.page.entity.SyncSocialRequest
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.presentation.setting.sync_social.item_sync_social.SyncSocialViewEntity
import com.twitter.sdk.android.core.TwitterAuthToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SyncSocialViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val userId: String,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onFacebookPageProfileResult = MutableSharedFlow<List<SyncSocialRequest>>()

    val onSuccess = MutableSharedFlow<Triple<Boolean, SyncSocialEntity, UserEntity?>>()

    val views = database.syncSocial().retrieve(userId = userId).map { map ->
        val facebook = SyncSocialViewEntity(
            map.find { it.provider is SocialType.Facebook },
            SocialType.Facebook,
        )
        val twitter = SyncSocialViewEntity(
            map.find { it.provider is SocialType.Twitter },
            SocialType.Twitter,
        )
        listOf(facebook, twitter)
    }

    init {
        logoutFacebook()
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

    fun syncWithFacebook(body: SyncSocialRequest) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { updateCurrentUser(result = it) },
        ) {
            val result = userRepository.syncWithFacebook(body = body, userId = userId)
            authenticationRepository.loginOutFacebook()
            Triple(result.first, result.second, database.user().get(userId).firstOrNull())
        }
    }

    fun syncWithTwitter(token: TwitterAuthToken?) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { updateCurrentUser(result = it) },
        ) {
            val result = userRepository.syncWithTwitter(token = token, userId = userId)
            Triple(result.first, result.second, database.user().get(userId).firstOrNull())
        }
    }

    private fun updateCurrentUser(result: Triple<Boolean, SyncSocialEntity, UserEntity?>) {
        launch(
            onError = { onSuccess.emitOnSuspend(result) },
            onSuccess = { onSuccess.emitOnSuspend(result) },
        ) {
            userRepository.fetchUserPage()
            userRepository.fetchUserProfile()
        }
    }

}