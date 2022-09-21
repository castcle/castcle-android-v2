package com.castcle.android.presentation.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.isLowerAndUpperCase
import com.castcle.android.core.extensions.isMinCharacters
import com.castcle.android.data.authentication.entity.AuthExistResponse
import com.castcle.android.data.authentication.entity.EmailIsExistRequest
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
//  Created by sklim on 5/9/2022 AD at 12:47.

@KoinViewModel
class SignUpViewModel(
    private val repository: AuthenticationRepository,
) : BaseViewModel() {

    val emailState = MutableLiveData<Boolean>()

    val passwordState = MutableLiveData<Boolean>()

    var emailVerifyUiState = MutableStateFlow<BaseUiState<AuthExistResponse>?>(null)

    val passwordUiState = MutableStateFlow<VerifyPasswordUiState?>(null)

    private var _password = MutableLiveData<String>()
    private var _confirmPassword = MutableLiveData<String>()

    fun onCheckEmailIsExist(email: String) {
        launch(onError = {
            emailVerifyUiState.value = BaseUiState.Error(it)
        }) {
            repository.emailIsExist(EmailIsExistRequest(email = email))
                .collectLatest {
                    viewModelScope.launch {
                        emailVerifyUiState.emit(it)
                    }
                }
        }
    }

    fun createPassword(password: String? = null, confirmPass: String? = null) {

        viewModelScope.launch {

            password?.let {
                _password.value = it
                when {
                    password.isEmpty() -> {
                        passwordUiState.emit(VerifyPasswordUiState.Default)
                    }
                    !password.isMinCharacters() && !password.isLowerAndUpperCase() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharactersMinimumError)
                        passwordUiState.emit(VerifyPasswordUiState.OnCharacterUpperLowerError)
                    }
                    !password.isMinCharacters() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharactersMinimumError)
                    }
                    !password.isLowerAndUpperCase() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharacterUpperLowerError)
                    }
                    password.isMinCharacters() && password.isLowerAndUpperCase() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharactersConditionPass)
                    }
                    password.isMinCharacters() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharactersMinimumPass)
                    }
                    password.isLowerAndUpperCase() -> {
                        passwordUiState.emit(VerifyPasswordUiState.OnCharacterUpperLowerPass)
                    }
                }
            }
        }

        viewModelScope.launch {
            confirmPass?.let {
                _confirmPassword.value = it
                if (it == _password.value && it.isMinCharacters() &&
                    _confirmPassword.value?.isLowerAndUpperCase() == true
                ) {
                    passwordUiState.emit(VerifyPasswordUiState.OnPasswordMatch)
                } else {
                    passwordUiState.emit(VerifyPasswordUiState.OnPasswordNotMatch)
                }
            }
        }
    }

}

sealed class VerifyPasswordUiState() {
    object Default : VerifyPasswordUiState()
    object OnCharactersConditionPass : VerifyPasswordUiState()
    object OnCharactersMinimumPass : VerifyPasswordUiState()
    object OnCharacterUpperLowerPass : VerifyPasswordUiState()
    object OnPasswordMatch : VerifyPasswordUiState()

    object OnCharactersMinimumError : VerifyPasswordUiState()
    object OnCharacterUpperLowerError : VerifyPasswordUiState()
    object OnPasswordNotMatch : VerifyPasswordUiState()
}
