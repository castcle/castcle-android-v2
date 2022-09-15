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

package com.castcle.android.presentation.setting.setting

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.timer
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.notification.NotificationRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val database: CastcleDatabase,
    private val mapper: SettingMapper,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val logoutComplete = MutableLiveData<Unit>()

    val logoutError = MutableLiveData<Throwable>()

    private var userUpdater: Job? = null

    init {
        startUserUpdater()
    }

    val views = database.user().retrieveWithSyncSocial()
        .combine(database.notificationBadges().retrieve(), mapper::map)
        .distinctUntilChanged()

    fun fetchData() {
        fetchNotificationBadges()
        fetchUserPage()
        fetchUserProfile()
    }

    private fun fetchNotificationBadges() {
        launch {
            notificationRepository.fetchNotificationsBadges()
        }
    }

    private fun fetchUserPage() {
        launch {
            userRepository.fetchUserPage()
        }
    }

    private fun fetchUserProfile() {
        launch {
            userRepository.fetchUserProfile()
        }
    }

    private fun startUserUpdater() {
        userUpdater = launch {
            timer(delay = 5_000).collectLatest {
                if (database.user().get(UserType.People).firstOrNull()?.isNotVerified() == true) {
                    fetchUserProfile()
                } else {
                    userUpdater?.cancel()
                }
            }
        }
    }

    fun logout() {
        launch(onError = logoutError::postValue) {
            authenticationRepository.unregisterFirebaseMessagingToken()
            authenticationRepository.loginOut()
            logoutComplete.postValue(Unit)
        }
    }

}