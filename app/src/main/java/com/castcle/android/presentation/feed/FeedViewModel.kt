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

package com.castcle.android.presentation.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.castcle.android.core.api.FeedApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_LARGE_ITEM
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.feed.data_source.FeedRemoteMediator
import com.castcle.android.data.feed.mapper.FeedResponseMapper
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.feed.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FeedViewModel(
    private val api: FeedApi,
    private val database: CastcleDatabase,
    private val feedMapper: FeedMapper,
    private val feedResponseMapper: FeedResponseMapper,
    private val glidePreloader: GlidePreloader,
    private val repository: FeedRepository,
    private val state: SavedStateHandle,
) : BaseViewModel() {

    init {
        clearDatabase()
    }

    private val contentEngagement = mutableListOf<Pair<String, String>>()

    val isGuest = database.accessToken()
        .retrieve()
        .map { it?.isGuest() ?: true }
        .distinctUntilChanged()

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = database.accessToken()
        .retrieve()
        .distinctUntilChanged()
        .flatMapLatest { accessToken ->
            Pager(
                config = PagingConfig(
                    initialLoadSize = PARAMETER_MAX_RESULTS_LARGE_ITEM,
                    pageSize = PARAMETER_MAX_RESULTS_LARGE_ITEM,
                ), pagingSourceFactory = {
                    database.feed().pagingSource()
                }, remoteMediator = FeedRemoteMediator(
                    api = api,
                    database = database,
                    glidePreloader = glidePreloader,
                    isGuest = accessToken?.isGuest() ?: true,
                    mapper = feedResponseMapper,
                )
            ).flow.map { pagingData ->
                pagingData.map { feedMapper.apply(it) }
            }
        }.cachedIn(viewModelScope)

    private fun clearDatabase() {
        launch {
            database.withTransaction {
                database.comment().delete()
                database.content().delete()
                database.followingFollowers().delete()
                database.profile().delete()
                database.loadKey().delete(LoadKeyType.Profile)
            }
        }
    }

    fun contentOffViewSeen(offView: String, seen: String) {
        launch {
            if (!contentEngagement.contains(offView to seen)) {
                contentEngagement.add(offView to seen)
                repository.contentOffView(offView)
                repository.contentSeen(seen)
            }
        }
    }

    fun showReportingContent(id: String, ignoreReportContentId: List<String>) {
        launch {
            database.feed().updateIgnoreReportContentId(id, ignoreReportContentId)
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