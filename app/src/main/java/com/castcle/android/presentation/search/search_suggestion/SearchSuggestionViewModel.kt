package com.castcle.android.presentation.search.search_suggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.error
import com.castcle.android.core.extensions.loading
import com.castcle.android.core.storage.database.CastcleDatabase
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