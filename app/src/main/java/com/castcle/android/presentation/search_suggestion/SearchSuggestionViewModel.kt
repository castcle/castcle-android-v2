package com.castcle.android.presentation.search_suggestion

import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.view_model.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchSuggestionViewModel(
    private val state: SavedStateHandle,
) : BaseViewModel() {

    fun saveItemsState(layoutManager: RecyclerView.LayoutManager) {
        state.set(SAVE_STATE_RECYCLER_VIEW, layoutManager.onSaveInstanceState())
    }

    fun restoreItemsState(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.onRestoreInstanceState(state.get(SAVE_STATE_RECYCLER_VIEW))
    }

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }

}