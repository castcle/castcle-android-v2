package com.castcle.android.presentation.world_trends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewEntity
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.presentation.world_trends.item_top_trends_item.TopTrendsItemViewEntity
import com.castcle.android.presentation.world_trends.item_top_trends_search.TopTrendsSearchViewEntity
import com.castcle.android.presentation.world_trends.item_top_trends_title.TopTrendsTitleViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopTrendsViewModel(
    private val repository: SearchRepository,
    private val state: SavedStateHandle,
) : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    init {
        getTopTrends()
    }

    private fun getTopTrends() {
        launch(onError = {
            val errorItems = ErrorStateViewEntity(error = it, retryAction = { getTopTrends() })
            views.postValue(listOf(errorItems))
        }) {
            val loadingItems = LoadingViewEntity()
            views.postValue(listOf(loadingItems))
            val items = repository.getTopTrends().map { TopTrendsItemViewEntity.map(it) }
            val titleItems = TopTrendsTitleViewEntity()
            val searchItems = TopTrendsSearchViewEntity()
            views.postValue(listOf(searchItems).plus(titleItems).plus(items))
        }
    }

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