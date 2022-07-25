package com.castcle.android.presentation.following_followers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_SMALL_ITEM
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.user.data_source.FollowingFollowersRemoteMediator
import com.castcle.android.data.user.mapper.FollowingFollowersResponseMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FollowingFollowersViewModel(
    api: UserApi,
    private val database: CastcleDatabase,
    glidePreloader: GlidePreloader,
    private val followingFollowersMapper: FollowingFollowersMapper,
    followingFollowersResponseMapper: FollowingFollowersResponseMapper,
    isFollowing: Boolean,
    private val state: SavedStateHandle,
    userId: String,
) : BaseViewModel() {

    private val sessionId = System.currentTimeMillis()

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = Pager(
        config = PagingConfig(
            initialLoadSize = PARAMETER_MAX_RESULTS_SMALL_ITEM,
            pageSize = PARAMETER_MAX_RESULTS_SMALL_ITEM,
        ), pagingSourceFactory = {
            database.followingFollowers().pagingSource(sessionId)
        }, remoteMediator = FollowingFollowersRemoteMediator(
            api = api,
            database = database,
            glidePreloader = glidePreloader,
            isFollowing = isFollowing,
            mapper = followingFollowersResponseMapper,
            sessionId = sessionId,
            userId = userId,
        )
    ).flow.map { pagingData ->
        pagingData.map { followingFollowersMapper.apply(it) }
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