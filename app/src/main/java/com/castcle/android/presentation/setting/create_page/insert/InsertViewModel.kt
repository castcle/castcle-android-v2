package com.castcle.android.presentation.setting.create_page.insert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.isEmail
import com.castcle.android.data.authentication.entity.AuthExistResponse
import com.castcle.android.data.authentication.entity.EmailIsExistRequest
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.presentation.setting.create_page.insert.entity.InsertEntity
import com.castcle.android.presentation.setting.create_page.insert.item_insert_contract_number.ItemInsertContractNumberViewEntity
import com.castcle.android.presentation.setting.create_page.insert.item_insert_email.ItemInsertEmailViewEntity
import com.castcle.android.presentation.sign_up.update_profile.entity.Contract
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
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
//  Created by sklim on 12/9/2022 AD at 16:44.

@KoinViewModel
class InsertViewModel(
    private val repository: AuthenticationRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val itemView = MutableStateFlow<CastcleViewEntity?>(null)

    var updateUiState = MutableStateFlow<BaseUiState<Nothing>?>(null)

    var castcleID = MutableLiveData<String>()

    fun getItemView(insertEntity: InsertEntity) {
        castcleID.value = insertEntity.castcleID
        viewModelScope.launch {
            when (insertEntity) {
                is InsertEntity.InsertEmail -> {
                    itemView.emit(ItemInsertEmailViewEntity())
                }
                is InsertEntity.InsertContractNumber -> {
                    itemView.emit(ItemInsertContractNumberViewEntity())
                }
            }
        }
    }

    fun onCheckNumber(number: String) {
        viewModelScope.launch {
            if (itemView.value is ItemInsertContractNumberViewEntity) {
                when {
                    number.isBlank() -> {
                        (itemView.value as ItemInsertContractNumberViewEntity)
                            .apply {
                                isPass = false
                            }.also {
                                itemView.emit(it)
                            }
                    }
                    else -> {
                        (itemView.value as ItemInsertContractNumberViewEntity)
                            .apply {
                                isPass = true
                            }.also {
                                itemView.emit(it)
                            }
                    }
                }
            }
        }
    }

    fun onCheckEmail(email: String) {
        viewModelScope.launch {
            if (itemView.value is ItemInsertEmailViewEntity) {
                when {
                    email.isBlank() -> {
                        (itemView.value as ItemInsertEmailViewEntity)
                            .apply {
                                isExists = null
                                message = null
                            }.also {
                                itemView.emit(it)
                            }
                    }
                    !email.isEmail() -> {
                        (itemView.value as ItemInsertEmailViewEntity)
                            .apply {
                                isExists = null
                                message = R.string.fragment_sign_up_email_error_format
                            }.also {
                                itemView.emit(it)
                            }
                    }
                    email.isNotBlank() -> {
                        checkEmailIsExists(email)
                    }
                }
            }
        }
    }

    private fun checkEmailIsExists(email: String) {
        launch(onError = {
            updateUiState.value = BaseUiState.Error(it)
        }) {
            repository.emailIsExist(EmailIsExistRequest(email = email))
                .collectLatest { responseState ->
                    when (responseState) {
                        is BaseUiState.Success -> {
                            handlerResponseState(responseState)
                        }
                        else -> Unit
                    }
                }
        }
    }

    private fun handlerResponseState(responseState: BaseUiState.Success<AuthExistResponse>) {
        if (itemView.value is ItemInsertEmailViewEntity) {
            viewModelScope.launch {
                if (responseState.data?.payload?.exist == true) {
                    (itemView.value as ItemInsertEmailViewEntity)
                        .copy(
                            isExists = true,
                            message = R.string.fragment_sign_up_email_verify_error
                        ).also {
                            itemView.emit(it)
                        }
                } else {
                    (itemView.value as ItemInsertEmailViewEntity)
                        .copy(
                            isExists = false,
                            message = null
                        ).also {
                            itemView.emit(it)
                        }
                }
            }
        }
    }

    fun saveEmail(email: String) {
        onUpdateDetail(
            UserUpdateRequest(
                currentCastcleId = castcleID.value,
                contract = Contract(
                    email = email
                )
            )
        )
    }

    fun saveContractNumber(number: String) {
        onUpdateDetail(
            UserUpdateRequest(
                currentCastcleId = castcleID.value,
                contract = Contract(
                    phone = number
                )
            )
        )
    }

    private fun onUpdateDetail(userUpdateRequest: UserUpdateRequest) {
        viewModelScope.launch {
            launch(onError = {
                viewModelScope.launch {
                    updateUiState.emit(BaseUiState.Error(it))
                }
            }) {
                userRepository.updateDetailProfile(userUpdateRequest)
                    .collectLatest {
                        updateUiState.emit(it)
                    }
            }
        }
    }
}
