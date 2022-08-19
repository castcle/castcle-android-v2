package com.castcle.android.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.UserApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_MEDIUM_ITEM
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_SMALL_ITEM
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.RetryException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.user.data_source.ProfileRemoteMediator
import com.castcle.android.data.user.mapper.ProfileResponseMapper
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val api: UserApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val profileMapper: ProfileMapper,
    private val repository: UserRepository,
    private val state: SavedStateHandle,
    private val profileResponseMapper: ProfileResponseMapper,
    private val userId: String,
) : BaseViewModel() {

    private val sessionId = System.currentTimeMillis()

    val currentUser = MutableStateFlow<UserEntity?>(null)

    val loadState = MutableStateFlow<LoadState>(LoadState.Loading)

    init {
        getUser()
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = currentUser.filterNotNull()
        .distinctUntilChangedBy { it.id }
        .flatMapLatest { user ->
            Pager(
                config = PagingConfig(
                    initialLoadSize = PARAMETER_MAX_RESULTS_MEDIUM_ITEM,
                    pageSize = PARAMETER_MAX_RESULTS_MEDIUM_ITEM,
                ), pagingSourceFactory = {
                    database.profile().pagingSource(sessionId)
                }, remoteMediator = ProfileRemoteMediator(
                    api = api,
                    database = database,
                    glidePreloader = glidePreloader,
                    mapper = profileResponseMapper,
                    sessionId = sessionId,
                    user = user,
                )
            ).flow.map { pagingData ->
                pagingData.map { profileMapper.apply(it) }
            }
        }.cachedIn(viewModelScope)

    fun fetchUser() {
        launch {
            repository.getUser(userId)
        }
    }

    private fun getUser() {
        launch({
            loadState.value = RetryException.loadState(it) { getUser() }
        }) {
            loadState.value = LoadState.Loading
            currentUser.value = repository.getUser(userId)
        }
    }

    fun showReportingContent(id: String, ignoreReportContentId: List<String>) {
        launch {
            database.profile().updateIgnoreReportContentId(id, ignoreReportContentId)
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