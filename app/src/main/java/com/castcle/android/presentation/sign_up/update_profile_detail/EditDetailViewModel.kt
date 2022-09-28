package com.castcle.android.presentation.sign_up.update_profile_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.entity.CreateUserState
import com.castcle.android.presentation.sign_up.update_profile.entity.UserUpdateRequest
import com.castcle.android.presentation.sign_up.update_profile_detail.item_edit_new_profile.EditProfileViewEntity
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
//  Created by sklim on 7/9/2022 AD at 17:31.
@KoinViewModel
class EditDetailViewModel(
    private val userRepository: UserRepository,
    private val database: CastcleDatabase,
) : BaseViewModel() {

    var createUserState = MutableLiveData(CreateUserState.PROFILE_CREATE)

    val editProfileItem = MutableStateFlow(EditProfileViewEntity())

    val updateUiState = MutableStateFlow<BaseUiState<Nothing>?>(null)

    val userEntity = MutableStateFlow(UserEntity())

    val castcleId = MutableLiveData<String>()

    val overviewStatePass = MutableLiveData(false)

    fun getUserLocal(castcleId: String) {
        launch {
            database.user().getByCastcleID(castcleId).collectLatest { it ->
                userEntity.emit(it ?: UserEntity())
                editProfileItem.value.copy(
                    userEntity = it ?: UserEntity()
                ).also {
                    editProfileItem.emit(it)
                }
            }
        }
    }

    fun onSetBirthDate(birthDate: String) {
        viewModelScope.launch {
            editProfileItem.value.copy(
                birthDate = birthDate
            ).also {
                editProfileItem.emit(it)
            }
        }
    }

    fun onUpdateDetail(userUpdateRequest: UserUpdateRequest) {
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