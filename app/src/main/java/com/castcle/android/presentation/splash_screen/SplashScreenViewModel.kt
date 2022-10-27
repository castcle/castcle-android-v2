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

package com.castcle.android.presentation.splash_screen

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.authentication.type.AccessTokenType
import com.castcle.android.domain.setting.SettingRepository
import com.castcle.android.domain.setting.entity.ConfigEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val settingRepository: SettingRepository,
) : BaseViewModel() {

    val config = MutableStateFlow<ConfigEntity?>(null)

    val fetchAccessTokenComplete = MutableSharedFlow<Unit>()

    private var fetchAccessTokenJob: Job? = null

    val onError = MutableSharedFlow<Throwable>()

    init {
        fetchFirebaseRemoteConfig()
    }

    fun fetchAccessToken() {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onLaunch = { fetchAccessTokenJob?.cancel() },
            onSuccess = { fetchAccessTokenComplete.emitOnSuspend() },
        ) {
            if (authenticationRepository.getAccessToken().type is AccessTokenType.Guest) {
                authenticationRepository.fetchGuestAccessToken()
            }
        }
    }

    private fun fetchFirebaseRemoteConfig() {
        launch(
            onError = { fetchAccessToken() },
            onLaunch = { startFetchAccessTokenJob() },
            onSuccess = { fetchAccessTokenJob?.cancel() },
        ) {
            config.value = settingRepository.fetchFirebaseRemoteConfig()
        }
    }

    private fun startFetchAccessTokenJob() {
        fetchAccessTokenJob?.cancel()
        fetchAccessTokenJob = launch {
            delay(5000)
            fetchAccessToken()
        }
    }

    override fun onCleared() {
        fetchAccessTokenJob?.cancel()
        super.onCleared()
    }

}