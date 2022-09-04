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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_MEDIUM_ITEM
import com.castcle.android.core.custom_view.load_state.item_empty_state_profile.EmptyStateProfileViewEntity
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.CastcleException
import com.castcle.android.core.error.RetryException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.user.data_source.ProfileRemoteMediator
import com.castcle.android.data.user.mapper.ProfileResponseMapper
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
) : BaseViewModel() {

    private val sessionId = System.currentTimeMillis()

    val currentUser = MutableStateFlow<UserEntity?>(null)

    val loadState = MutableSharedFlow<LoadState>()

    init {
        getUser()
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = currentUser.filterNotNull()
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
                pagingData.map { profileMapper.apply(it) }
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
            if (userInDatabase != null) {
                currentUser.value = userInDatabase
            } else {
                currentUser.value = repository.getUser(userId)
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

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }

}