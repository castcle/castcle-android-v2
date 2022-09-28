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

package com.castcle.android.presentation.profile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_MEDIUM_ITEM
import com.castcle.android.core.custom_view.load_state.item_empty_state_profile.EmptyStateProfileViewEntity
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.CastcleException
import com.castcle.android.core.error.RetryException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.work.ImageUploaderWorkHelper
import com.castcle.android.core.work.StateWorkLoading
import com.castcle.android.data.user.data_source.ProfileRemoteMediator
import com.castcle.android.data.user.mapper.ProfileResponseMapper
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.ProfileType
import com.castcle.android.presentation.profile.item_profile_page.ProfilePageViewEntity
import com.castcle.android.presentation.profile.item_profile_user.ProfileUserViewEntity
import com.castcle.android.presentation.sign_up.update_profile.entity.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val api: UserApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val profileMapper: ProfileMapper,
    private val repository: UserRepository,
    private val state: SavedStateHandle,
    private val profileResponseMapper: ProfileResponseMapper,
    private val userId: String,
    private val imageUploaderWorkHelper: ImageUploaderWorkHelper,
) : BaseViewModel() {

    private val sessionId = System.currentTimeMillis()

    val currentUser = MutableStateFlow<UserEntity?>(null)

    val userItemView = MutableStateFlow<ProfileUserViewEntity?>(null)

    val pageItemView = MutableStateFlow<ProfilePageViewEntity?>(null)

    val loadState = MutableSharedFlow<LoadState>()

    val selectState = MutableStateFlow<PhotoSelectedState?>(null)

    private val imageRequest = MutableStateFlow(UpLoadImagesRequest())

    val imageAvatarUri = MutableStateFlow<Uri?>(null)

    val imageCoverUri = MutableStateFlow<Uri?>(null)

    init {
        getUser()
        initImageUpLoad()
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views: Flow<PagingData<CastcleViewEntity>> = currentUser.filterNotNull()
        .distinctUntilChangedBy { it.id }
        .flatMapLatest { user ->
            Pager(
                config = PagingConfig(
                    initialLoadSize = PARAMETER_MAX_RESULTS_MEDIUM_ITEM,
                    pageSize = PARAMETER_MAX_RESULTS_MEDIUM_ITEM,
                ), pagingSourceFactory = {
                    database.profile().pagingSource(sessionId)
                }, remoteMediator = ProfileRemoteMediator(
                    api = api,
                    database = database,
                    glidePreloader = glidePreloader,
                    mapper = profileResponseMapper,
                    sessionId = sessionId,
                    user = user,
                )
            ).flow.map { pagingData ->
                pagingData
                    .filter {
                        it.profile.type != ProfileType.Profile
                    }.map {
                        profileMapper.apply(it)
                    }
            }
        }.cachedIn(viewModelScope)

    fun fetchUser() {
        launch {
            repository.getUser(userId)
        }
    }

    private fun getUser() {
        launch(
            onError = {
                if (it is CastcleException.UserNotFoundException) {
                    loadState.emitOnSuspend(
                        RetryException.loadState(
                            error = it,
                            errorItems = EmptyStateProfileViewEntity.create(1),
                            retryAction = { getUser() },
                        )
                    )
                } else {
                    loadState.emitOnSuspend(RetryException.loadState(it) { getUser() })
                }
            },
            onLaunch = {
                loadState.emitOnSuspend(LoadState.Loading)
            },
        ) {
            val userInDatabase = database.user().get(userId).firstOrNull()
            val user = userInDatabase ?: repository.getUser(userId)
            database.user().getByUserID(user.id).filterNotNull().collectLatest {
                currentUser.value = it
                handlerProfileView(profileMapper.applyUserItemView(it), it)
            }
        }
    }

    private fun handlerProfileView(itemView: CastcleViewEntity, userEntity: UserEntity) {
        viewModelScope.launch {
            when {
                userItemView.value != null -> {
                    userItemView.value?.copy(
                        user = userEntity
                    ).also {
                        userItemView.emit(it)
                    }
                }
                itemView is ProfileUserViewEntity -> {
                    userItemView.emit(itemView)
                }
                pageItemView.value != null -> {
                    pageItemView.value?.copy(
                        user = userEntity
                    ).also {
                        pageItemView.emit(it)
                    }
                }
                itemView is ProfilePageViewEntity -> {
                    pageItemView.emit(itemView)
                }
            }
        }
    }

    private fun initImageUpLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            imageAvatarUri
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { image ->
                    handlerImageUpLoad(imageAvatar = image)
                    imageRequest.value.copy(
                        avatar = image.toString()
                    ).also {
                        viewModelScope.launch(Dispatchers.Main) {
                            imageRequest.value = it
                        }
                    }
                }
        }

        viewModelScope.launch {
            imageCoverUri
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { image ->
                    handlerImageUpLoad(imageCover = image)
                    imageRequest.value.copy(
                        cover = image.toString()
                    ).also {
                        imageRequest.value = it
                    }
                }
        }

        viewModelScope.launch {
            imageRequest.drop(1)
                .distinctUntilChangedBy { it }
                .collectLatest {
                    upLoadImageAvatarOrCover(it)
                }
        }
    }

    fun onStopLoadingImage() {
        viewModelScope.launch {
            if (userItemView.value != null) {
                userItemView.value?.copy(
                    onUploadAvatar = false,
                    onUploadCover = false
                ).also {
                    userItemView.emit(it)
                }
            }
            if (pageItemView.value != null) {
                pageItemView.value?.copy(
                    onUploadAvatar = false,
                    onUploadCover = false
                ).also {
                    pageItemView.emit(it)
                }
            }
        }
    }

    private suspend fun handlerImageUpLoad(imageAvatar: Uri? = null, imageCover: Uri? = null) {
        withContext(Dispatchers.IO) {
            imageAvatar?.let { it ->
                if (userItemView.value != null) {
                    userItemView.value?.copy(
                        onUploadAvatar = true,
                        avatarUpLoad = it
                    ).also {
                        userItemView.emit(it)
                    }
                }
                if (pageItemView.value != null) {
                    pageItemView.value?.copy(
                        onUploadAvatar = true,
                        avatarUpLoad = it
                    ).also {
                        pageItemView.emit(it)
                    }
                }

            }
            imageCover?.let { it ->
                if (userItemView.value != null) {
                    userItemView.value?.copy(
                        onUploadCover = true,
                        coveUpLoad = it
                    ).also {
                        userItemView.emit(it)
                    }
                }

                if (pageItemView.value != null) {
                    pageItemView.value?.copy(
                        onUploadCover = true,
                        coveUpLoad = it
                    ).also {
                        pageItemView.emit(it)
                    }
                }
            }
        }
    }

    fun showReportingContent(id: String, ignoreReportContentId: List<String>) {
        launch {
            database.profile().updateIgnoreReportContentId(id, ignoreReportContentId)
        }
    }

    fun saveItemsState(layoutManager: RecyclerView.LayoutManager) {
        state[SAVE_STATE_RECYCLER_VIEW] = layoutManager.onSaveInstanceState()
    }

    fun restoreItemsState(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.onRestoreInstanceState(state[SAVE_STATE_RECYCLER_VIEW])
    }

    fun upLoadImageAvatarOrCover(imageRequest: UpLoadImagesRequest) {
        imageUploaderWorkHelper.uploadImage(imageRequest.toStringImageRequest())
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

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }

}