package com.castcle.android.presentation.search_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_LARGE_ITEM
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_SMALL_ITEM
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
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
        PARAMETER_MAX_RESULTS_LARGE_ITEM
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
        pagingData.map { searchResultMapper.apply(it, type) }
    }.cachedIn(viewModelScope)

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