package com.castcle.android.presentation.profile.edit_profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.isEngText
import com.castcle.android.core.extensions.toISO8601
import com.castcle.android.core.work.ImageUploaderWorkHelper
import com.castcle.android.data.authentication.entity.CastcleIdExistRequest
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.data.user.entity.UserLinkResponse
import com.castcle.android.domain.authentication.AuthenticationRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.sign_up.entity.CASTCLE_CHAR_LIMIT
import com.castcle.android.presentation.sign_up.entity.VerifyProfileState
import com.castcle.android.presentation.sign_up.update_profile.entity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
//  Created by sklim on 25/9/2022 AD at 16:16.

@KoinViewModel
class EditProfileViewModel(
    private val imageUploaderWorkHelper: ImageUploaderWorkHelper,
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val mapper: EditMapper,
    private val state: SavedStateHandle
) : BaseViewModel() {

    val userType = MutableLiveData<UserType>()

    val itemView = MutableStateFlow<ItemEditEntity?>(null)

    val imageRequest = MutableLiveData<UpLoadImagesRequest>()

    val updateUiState = MutableStateFlow<BaseUiState<Nothing>?>(null)

    val userEntity = MutableStateFlow(UserEntity())

    val selectState = MutableStateFlow<PhotoSelectedState?>(null)

    val imageAvatarUri = MutableStateFlow<Uri?>(null)

    val imageCoverUri = MutableStateFlow<Uri?>(null)

    val overviewStatePass = MutableStateFlow(true)

    var inputUiState = MutableStateFlow<VerifyProfileState?>(null)

    init {
        initImageItem()
    }

    private fun initImageItem() {
        viewModelScope.launch {
            imageAvatarUri
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { image ->
                    itemView.value?.copy(
                        avatarUpLoad = image
                    ).also {
                        itemView.emit(it)
                    }
                    if (imageRequest.value != null) {
                        imageRequest.value?.copy(
                            avatar = image.toString()
                        )
                    } else {
                        UpLoadImagesRequest(
                            avatar = image.toString(),
                            castcleId = userEntity.value.castcleId
                        )
                    }.also {
                        imageRequest.value = it
                    }
                }
        }

        viewModelScope.launch {
            imageCoverUri
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { image ->
                    itemView.value?.copy(
                        coveUpLoad = image
                    ).also {
                        itemView.emit(it)
                    }
                    if (imageRequest.value != null) {
                        imageRequest.value?.copy(
                            cover = image.toString(),
                            castcleId = userEntity.value.castcleId
                        )
                    } else {
                        UpLoadImagesRequest(cover = image.toString())
                    }.also {
                        imageRequest.value = it
                    }
                }
        }
    }

    fun fetchProfile(userId: String) {
        launch {
            userRepository.getUserFlow(userId)
                .onEach {
                    userEntity.value = it ?: UserEntity()
                }.mapNotNull {
                    mapper.apply(it ?: UserEntity())
                }.collectLatest {
                    itemView.emit(it)
                }
        }
    }

    fun onUpdateProfile() {
        viewModelScope.launch {
            itemView.value?.let {
                getUpDateUserRequest(it)
            }.also {
                it?.apply {
                    currentCastcleId = userEntity.value.castcleId
                }.run(::onCallUpdate)
            }
        }
    }

    private fun onCallUpdate(userUpdateRequest: UserUpdateRequest?) {
        launch(onError = {
            viewModelScope.launch {
                updateUiState.emit(BaseUiState.Error(it))
            }
        }) {
            userRepository.updateDetailProfile(
                userUpdateRequest ?: UserUpdateRequest()
            ).collectLatest {
                updateUiState.emit(it)
            }
        }

        launch {
            imageRequest.value?.let {
                upLoadImageAvatarOrCover(it)
            }
        }
    }

    private fun getUpDateUserRequest(itemEditEntity: ItemEditEntity): UserUpdateRequest {
        return UserUpdateRequest(
            castcleIdEdit = itemEditEntity.castcleId,
            displayName = itemEditEntity.displayName,
            overview = itemEditEntity.overview,
            dob = itemEditEntity.birthDate?.toISO8601(),
            links = UserLinkResponse(
                facebook = itemEditEntity.linkFacebook,
                twitter = itemEditEntity.linkTwitter,
                youtube = itemEditEntity.linkYoutube,
                website = itemEditEntity.linkWeb,
                medium = itemEditEntity.linkMedium,
            )
        )
    }

    private fun upLoadImageAvatarOrCover(imageRequest: UpLoadImagesRequest) {
        imageUploaderWorkHelper.uploadImage(imageRequest.toStringImageRequest())
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
                userEntity.value.canUpdateCastcleId == true -> {
                    checkCastcleIdIsExist(castcleID).join()
                }
                else -> Unit
            }
        }
    }

    private suspend fun checkCastcleIdIsExist(castcleID: String): Job {
        return viewModelScope.async(start = CoroutineStart.LAZY) {
            launch {
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

    fun handlerOnCastcleIdEdit(condition: Boolean, onPass: () -> Unit, onFail: () -> Unit) {
        when {
            inputUiState.value != null && userEntity.value.canUpdateCastcleId == true -> {
                if (condition && inputUiState.value == VerifyProfileState.CASTCLE_ID_PASS &&
                    itemView.value?.castcleId != userEntity.value.castcleId
                ) {
                    onPass.invoke()
                } else {
                    onFail.invoke()
                }
            }
            overviewStatePass.value &&
                (itemView.value?.linkFacebook != userEntity.value.linkFacebook ||
                    itemView.value?.linkTwitter != userEntity.value.linkTwitter ||
                    itemView.value?.linkYoutube != userEntity.value.linkYoutube ||
                    itemView.value?.linkMedium != userEntity.value.linkMedium ||
                    itemView.value?.linkWeb != userEntity.value.linkWebsite ||
                    itemView.value?.castcleId != userEntity.value.castcleId ||
                    itemView.value?.overview != userEntity.value.overview ||
                    itemView.value?.avatarUpLoad != userEntity.value.avatar.original.toUri() ||
                    itemView.value?.coveUpLoad != userEntity.value.cover?.original?.toUri() ||
                    itemView.value?.displayName != userEntity.value.displayName) -> {
                onPass.invoke()
            }
            else -> {
                onFail.invoke()
            }
        }
    }

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }
}