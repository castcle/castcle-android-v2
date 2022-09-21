package com.castcle.android.presentation.setting.ads.ads_manage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.error.RetryException
import com.castcle.android.data.ads.AdvertiseRepositoryImpl
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.ads.type.AdFilterType
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
//  Created by sklim on 15/9/2022 AD at 13:40.

@KoinViewModel
class AdsManageViewModel(
    private val repository: AdvertiseRepositoryImpl
) : BaseViewModel() {

    val filter = MutableStateFlow<AdFilterType>(AdFilterType.All)

    val itemView = MutableLiveData<List<CastcleViewEntity>>()

    val loadState = MutableSharedFlow<LoadState>()

    init {
        onFilterChange()
        getHistoryOnChange()
    }

    private fun onFilterChange() {
        launch {
            filter.drop(1).filterNotNull()
                .distinctUntilChangedBy { it }
                .collectLatest {
                    refreshData()
                }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.onRefreshAdvertiseHistory().onJoin
        }
        fetchHistory(adFilterType = filter.value)
    }

    private fun fetchHistory(adFilterType: AdFilterType) {
        launch(onError = {
            loadState.emitOnSuspend(RetryException.loadState(it) { fetchHistory(filter.value) })
        }) {
            repository.fetchAdvertiseHistory(adFilterType.filter).collectLatest {
                when (it) {
                    is BaseUiState.Loading -> {
                        if (it.isLoading) {
                            loadState.emitOnSuspend(LoadState.Loading)
                        } else {
                            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getHistoryOnChange() {
        launch(onLaunch = {
            loadState.emitOnSuspend(LoadState.Loading)
        }, onSuccess = {
            loadState.emitOnSuspend(LoadState.NotLoading(endOfPaginationReached = true))
        }) {
            refreshData()
            repository.getAdvertiseHistory()
                .map {
                    it.map { advertise ->
                        advertise.toItemAdvertiseViewEntity()
                    }
                }.collectLatest(itemView::postValue)
        }
    }
}