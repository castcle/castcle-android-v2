package com.castcle.android.presentation.sign_up.update_profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.work.ImageUploaderWorkHelper
import com.castcle.android.core.work.StateWorkLoading
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.sign_up.update_profile.entity.*
import com.castcle.android.presentation.sign_up.update_profile.item_update_profile.UpdateProfileViewEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
//  Created by sklim on 6/9/2022 AD at 17:51.

@KoinViewModel
class UpdateProfileViewModel(
    private val imageUploaderWorkHelper: ImageUploaderWorkHelper,
    private val database: CastcleDatabase,
) : BaseViewModel() {

    val castcleId = MutableLiveData<String>()

    val selectState = MutableLiveData(PhotoSelectedState.NON)

    val imageAvatarUri = MutableStateFlow<Uri?>(null)

    val imageCoverUri = MutableStateFlow<Uri?>(null)

    val userEntity = MutableStateFlow(UserEntity())

    val updateProfileItem = MutableStateFlow(UpdateProfileViewEntity())

    init {
        initViewItem()
        initImageItem()
    }

    fun getUserLocal(castcleId: String) {
        launch {
            database.user().getByCastcleID(castcleId).collectLatest {
                userEntity.emit(it ?: UserEntity())
            }
        }
    }

    private fun upLoadImageAvatarOrCover(imageRequest: UpLoadImagesRequest) {
        imageUploaderWorkHelper.uploadImage(imageRequest.toStringImageRequest())
    }

    private fun initViewItem() {
        viewModelScope.launch {
            userEntity.filterNotNull()
                .collectLatest {
                    val itemView = updateProfileItem.value.copy(
                        userEntity = it
                    )
                    viewModelScope.launch(Dispatchers.Main) {
                        updateProfileItem.emit(itemView)
                    }
                }
        }
    }

    private fun initImageItem() {
        viewModelScope.launch {
            imageAvatarUri
                .filterNotNull()
                .distinctUntilChanged()
                .map {
                    UpLoadImagesRequest(
                        avatar = it.toString(),
                        castcleId = userEntity.value.castcleId
                    )
                }.collectLatest { imageRequest ->
                    updateProfileItem.value.copy(
                        onUploadAvatar = true,
                        avatarUpLoad = imageRequest.avatar?.toUri()
                    ).also {
                        viewModelScope.launch(Dispatchers.Main) {
                            updateProfileItem.emit(it)
                        }
                    }
                    upLoadImageAvatarOrCover(imageRequest)
                }
        }

        viewModelScope.launch {
            imageCoverUri
                .filterNotNull()
                .distinctUntilChanged()
                .map {
                    UpLoadImagesRequest(
                        cover = it.toString(),
                        castcleId = userEntity.value.castcleId
                    )
                }.collectLatest { imageRequest ->
                    updateProfileItem.value.copy(
                        onUploadCover = true,
                        coveUpLoad = imageRequest.cover?.toUri()
                    ).also {
                        updateProfileItem.emit(it)
                    }
                    upLoadImageAvatarOrCover(imageRequest)
                }
        }
    }

    fun onStopLoading(avatarOrCover: Boolean = false) {
        viewModelScope.launch {
            if (avatarOrCover) {
                updateProfileItem.value.copy(
                    onUploadAvatar = false,
                ).also {
                    updateProfileItem.emit(it)
                }
            } else {
                updateProfileItem.value.copy(
                    onUploadCover = false,
                ).also {
                    updateProfileItem.emit(it)
                }
            }
        }
    }

    fun checkWorkUpLoadImage(): Flowable<StateWorkLoading>? {
        return imageUploaderWorkHelper.uploadStatus.map {
            when (it) {
                is ImageUploaderWorkHelper.Status.Error -> {
                    StateWorkLoading.ERROR
                }
                is ImageUploaderWorkHelper.Status.Uploading -> {
                    StateWorkLoading.NON
                }
                is ImageUploaderWorkHelper.Status.Success -> {
                    StateWorkLoading.SUCCESS
                }
                ImageUploaderWorkHelper.Status.LostInternetConnect -> {
                    StateWorkLoading.LOST_CONNECT
                }
            }
        }.toFlowable(BackpressureStrategy.LATEST)
    }

}
