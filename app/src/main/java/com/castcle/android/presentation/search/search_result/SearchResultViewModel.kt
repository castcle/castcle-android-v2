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

package com.castcle.android.presentation.search.search_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.*
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.search.data_source.SearchRemoteMediator
import com.castcle.android.data.search.mapper.SearchResponseMapper
import com.castcle.android.domain.search.type.SearchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchResultViewModel(
    api: SearchApi,
    private val database: CastcleDatabase,
    glidePreloader: GlidePreloader,
    private val sessionId: Long,
    searchResponseMapper: SearchResponseMapper,
    private val searchResultMapper: SearchResultMapper,
    private val state: SavedStateHandle,
    private val type: SearchType,
) : BaseViewModel() {

    private val pageSize = if (type is SearchType.People) {
        PARAMETER_MAX_RESULTS_SMALL_ITEM
    } else {
        PARAMETER_MAX_RESULTS_MEDIUM_ITEM
    }

    val keyword = database.searchKeyword().retrieve(sessionId)
        .map { it.firstOrNull()?.keyword.orEmpty() }
        .distinctUntilChanged()
        .drop(1)

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = Pager(
        config = PagingConfig(
            initialLoadSize = pageSize,
            pageSize = pageSize,
        ), pagingSourceFactory = {
            database.search().pagingSource(sessionId.plus(type.index))
        }, remoteMediator = SearchRemoteMediator(
            api = api,
            database = database,
            glidePreloader = glidePreloader,
            keywordSessionId = sessionId,
            mapper = searchResponseMapper,
            sessionId = sessionId.plus(type.index),
            type = type,
        )
    ).flow.map { pagingData ->
        pagingData.map { searchResultMapper.apply(it, type, database.config().get()) }
    }.cachedIn(viewModelScope)

    fun showReportingContent(id: String, ignoreReportContentId: List<String>) {
        launch {
            database.search().updateIgnoreReportContentId(id, ignoreReportContentId)
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