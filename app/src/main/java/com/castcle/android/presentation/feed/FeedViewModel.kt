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
    private val state: SavedStateHandle,
) : BaseViewModel() {

    init {
        clearDatabase()
    }

    val isGuest = database.accessToken()
        .retrieve()
        .map { it.firstOrNull()?.isGuest() ?: true }
        .distinctUntilChanged()

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = isGuest
        .distinctUntilChanged()
        .flatMapLatest { isGuest ->
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
                    isGuest = isGuest,
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