package com.castcle.android.presentation.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.castcle.android.core.api.FeedApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_DEFAULT
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.feed.data_source.FeedRemoteMediator
import com.castcle.android.data.feed.mapper.FeedResponseMapper
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.user.type.UserType
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

    private val currentUser = database.user()
        .retrieve(UserType.People)
        .map { it.firstOrNull() }
        .distinctUntilChangedBy { it?.id }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = isGuest.combine(currentUser) { isGuest, user -> isGuest to user }
        .distinctUntilChanged()
        .flatMapLatest { (isGuest, user) ->
            Pager(
                config = PagingConfig(
                    initialLoadSize = PARAMETER_MAX_RESULTS_DEFAULT,
                    pageSize = PARAMETER_MAX_RESULTS_DEFAULT,
                ), pagingSourceFactory = {
                    database.feed().pagingSource()
                }, remoteMediator = FeedRemoteMediator(
                    api = api,
                    database = database,
                    glidePreloader = glidePreloader,
                    isGuest = isGuest,
                    mapper = feedResponseMapper,
                    user = user,
                )
            ).flow.map { pagingData ->
                pagingData.map { feedMapper.apply(it) }
            }
        }.cachedIn(viewModelScope)

    private fun clearDatabase() {
        launch {
            database.withTransaction {
                database.profile().delete()
                database.loadKey().delete(LoadKeyType.Profile)
            }
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