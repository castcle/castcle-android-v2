package com.castcle.android.presentation.sign_up.create_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.isEngText
import com.castcle.android.data.authentication.entity.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.tracker.TrackerRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.create_profile.entity.RegisterRequest
import com.castcle.android.presentation.sign_up.entity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.annotation.KoinViewModel

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 6/9/2022 AD at 10:47.

@KoinViewModel
class NewProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val trackerRepository: TrackerRepository,
) : BaseViewModel() {

    val password = MutableLiveData<String?>(null)

    val email = MutableLiveData<String?>(null)

    var createUserState = MutableLiveData(CreateUserState.PROFILE_CREATE)

    private var castcleIDUiState = MutableStateFlow<BaseUiState<AuthExistResponse>?>(null)

    var suggestionUiState = MutableStateFlow<BaseUiState<String>?>(null)

    var inputUiState = MutableStateFlow<VerifyProfileState?>(null)

    var registerUiState = MutableStateFlow<BaseUiState<UserEntity>?>(null)

    private suspend fun checkCastcleIdIsExist(castcleID: String): Job {
        return viewModelScope.async(start = CoroutineStart.LAZY) {
            launch(onError = {
                castcleIDUiState.value = BaseUiState.Error(it)
            }) {
                authenticationRepository.castcleIdIsExist(
                    CastcleIdExistRequest(castcleID = castcleID)
                ).collectLatest {
                    viewModelScope.launch {
                        when (it) {
                            is BaseUiState.Success -> {
                                if (it.data?.payload?.exist == false) {
                                    inputUiState.emit(VerifyProfileState.CASTCLE_ID_PASS)
                                } else {
                                    inputUiState.emit(VerifyProfileState.CASTCLE_ID_ERROR)
                                }
                            }
                            is BaseUiState.Error -> {
                                inputUiState.emit(VerifyProfileState.CASTCLE_ID_ERROR)
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }.await()
    }

    fun getSuggestionCastcleID(displayName: String) {
        viewModelScope.launch {
            launch(onError = {
                suggestionUiState.value = BaseUiState.Error(it)
            }) {
                authenticationRepository.getSuggestionCastcleId(
                    DisplayNameRequest(displayName = displayName)
                ).collectLatest {
                    viewModelScope.launch {
                        when (it) {
                            is BaseUiState.Success -> {
                                suggestionUiState.emit(
                                    BaseUiState.Success(
                                        it.data?.suggestCastcleId ?: ""
                                    )
                                )
                            }
                            is BaseUiState.Error -> {
                                suggestionUiState.emit(
                                    BaseUiState.Error(it.exception)
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    fun handlerDisplayName(castcleID: String) {
        viewModelScope.launch {
            when {
                castcleID.isBlank() -> {
                    inputUiState.emit(VerifyProfileState.NONE)
                }
                castcleID.length >= CASTCLE_CHAR_LIMIT -> {
                    inputUiState.emit(VerifyProfileState.CASTCLE_ID_LENGHT_ERROR)
                }
                !castcleID.replace("@", "").isEngText() -> {
                    inputUiState.emit(VerifyProfileState.CASTCLE_ID_SPECIAL_ERROR)
                }
                else -> {
                    checkCastcleIdIsExist(castcleID).join()
                }
            }
        }
    }

    fun onRegisterWithEmail(register: RegisterRequest) {
        launch(onError = {
            suggestionUiState.value = BaseUiState.Error(it)
        }) {
            if (createUserState.value == CreateUserState.PROFILE_CREATE) {
                authenticationRepository
                    .registerWithEmail(register)
                    .collectLatest {
                        if (it is BaseUiState.Success) {
                            trackRegistration(it.data?.id.orEmpty())
                        }
                        registerUiState.emit(it)
                    }
            } else {
                authenticationRepository
                    .createPage(register)
                    .collectLatest {
                        registerUiState.emit(it)
                    }
            }
        }
    }

    private fun trackRegistration(userId: String) {
        launch {
            trackerRepository.trackRegistration(channel = "email", userId = userId)
        }
    }

}