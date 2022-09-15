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

package com.castcle.android.presentation.home

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val isGuest = MutableStateFlow(false)

    private val isUserNotVerified = MutableStateFlow(true)

    val recursiveRefreshToken = database.recursiveRefreshToken()
        .retrieve()
        .mapNotNull { it.firstOrNull() }

    init {
        isGuestUpdater()
        isUserVerifiedUpdater()
    }

    fun followUser(
        isGuestAction: () -> Unit,
        targetUser: UserEntity,
    ) {
        isUserCanEngagement(isGuestAction = isGuestAction) {
            launch {
                if (targetUser.followed) {
                    userRepository.unfollowUser(targetUser = targetUser)
                } else {
                    userRepository.followUser(targetUser = targetUser)
                }
            }
        }
    }

    private fun isGuestUpdater() {
        launch {
            database.accessToken().retrieve()
                .filterNotNull()
                .collectLatest { isGuest.value = it.isGuest() }
        }
    }

    fun isUserCanEngagement(
        isGuestAction: (() -> Unit)? = null,
        isUserNotVerifiedAction: (() -> Unit)? = null,
        isMemberAction: () -> Unit,
    ) {
        launch {
            if (!isGuest.value && isUserNotVerified.value) {
                userRepository.fetchUserProfile()
            }
            when {
                isGuestAction != null && isGuest.value -> isGuestAction()
                isUserNotVerifiedAction != null && isUserNotVerified.value -> isUserNotVerifiedAction()
                else -> isMemberAction()
            }
        }
    }

    fun isCanUseWallet(
        isCanUseAction: () -> Unit,
        isCantUseAction: () -> Unit,
    ) {
        launch {
            val user = database.user().get(UserType.People).firstOrNull()
            if (user?.verifiedEmail == true && user.verifiedMobile == true) {
                isCanUseAction()
            } else {
                val fetchUser = userRepository.fetchUserProfile()
                if (fetchUser.verifiedEmail == true && fetchUser.verifiedMobile == true) {
                    isCanUseAction()
                } else {
                    isCantUseAction()
                }
            }
        }
    }

    private fun isUserVerifiedUpdater() {
        launch {
            database.user().retrieve(UserType.People)
                .mapNotNull { it.firstOrNull() }
                .collectLatest { isUserNotVerified.value = it.isNotVerified() }
        }
    }

    fun likeCast(
        isGuestAction: () -> Unit = {},
        isUserNotVerifiedAction: () -> Unit = {},
        targetCast: CastEntity,
    ) {
        isUserCanEngagement(
            isGuestAction = isGuestAction,
            isUserNotVerifiedAction = isUserNotVerifiedAction,
        ) {
            launch {
                if (targetCast.liked) {
                    userRepository.unlikeCasts(content = targetCast)
                } else {
                    userRepository.likeCasts(content = targetCast)
                }
            }
        }
    }

    fun logout(onSuccessAction: () -> Unit) {
        launch(onSuccess = { onSuccessAction() }) {
            authenticationRepository.loginOut()
        }
    }

}