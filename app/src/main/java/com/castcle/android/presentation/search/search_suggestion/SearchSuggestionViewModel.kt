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

package com.castcle.android.presentation.search.search_suggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.error
import com.castcle.android.core.extensions.loading
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.SearchSuggestionEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchSuggestionViewModel(
    private val database: CastcleDatabase,
    private val mapper: SearchSuggestionMapper,
    private val repository: SearchRepository,
    private val state: SavedStateHandle,
) : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    private val resultCache = hashMapOf<String, SearchSuggestionEntity>()

    init {
        search("")
    }

    fun clearRecentSearch() {
        launch {
            database.recentSearch().delete()
            search("")
        }
    }

    fun search(keyword: String) {
        launch(onError = {
            views.error(it) { search(keyword) }
        }) {
            if (keyword.isNotBlank() && !resultCache.contains(keyword)) {
                views.loading()
                resultCache[keyword] = repository.searchByKeyword(keyword)
            }
            val result = if (keyword.isBlank()) {
                val recentSearch = database.recentSearch().get()
                mapper.map(recentSearch = recentSearch)
            } else {
                val searchSuggestion = resultCache[keyword] ?: SearchSuggestionEntity()
                mapper.map(searchSuggestion = searchSuggestion)
            }
            views.postValue(result)
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