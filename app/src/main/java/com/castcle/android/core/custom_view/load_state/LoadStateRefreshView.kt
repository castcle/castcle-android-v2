package com.castcle.android.core.custom_view.load_state

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.*
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType.*
import com.castcle.android.core.custom_view.load_state.item_empty_state_content.EmptyStateContentViewRenderer
import com.castcle.android.core.custom_view.load_state.item_empty_state_feed.EmptyStateFeedViewEntity
import com.castcle.android.core.custom_view.load_state.item_empty_state_feed.EmptyStateFeedViewRenderer
import com.castcle.android.core.custom_view.load_state.item_empty_state_profile.EmptyStateProfileViewRenderer
import com.castcle.android.core.custom_view.load_state.item_empty_state_search.EmptyStateSearchViewEntity
import com.castcle.android.core.custom_view.load_state.item_empty_state_search.EmptyStateSearchViewRenderer
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_comment.LoadingStateCommentViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_comment.LoadingStateCommentViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_profile.LoadingStateProfileViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_profile.LoadingStateProfileViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_user.LoadingStateUserViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_user.LoadingStateUserViewRenderer
import com.castcle.android.core.error.RetryException
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutLoadStateRefreshBinding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class LoadStateRefreshView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs), CastcleListener {

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(EmptyStateContentViewRenderer())
            registerRenderer(EmptyStateFeedViewRenderer())
            registerRenderer(EmptyStateProfileViewRenderer())
            registerRenderer(EmptyStateSearchViewRenderer())
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer())
            registerRenderer(LoadingStateCommentViewRenderer())
            registerRenderer(LoadingStateProfileViewRenderer())
            registerRenderer(LoadingStateUserViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutLoadStateRefreshBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val compositeDisposable = CompositeDisposable()

    private var isRefreshing = false

    private var stateItems = StateItems(
        empty = EmptyStateFeedViewEntity.create(1),
        error = EmptyStateFeedViewEntity.create(1),
        loading = LoadingStateCastViewEntity.create(3),
    )

    init {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
    }

    @FlowPreview
    suspend fun bind(
        adapter: CastcleAdapter,
        loadState: MutableSharedFlow<LoadState>,
        recyclerView: RecyclerView,
        type: LoadStateRefreshItemsType,
    ) {
        updateStateItems(type)
        loadState.toCombinedLoadStates()
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest {
                val itemCount = adapter.itemCount
                when {
                    it.isEmptyState(itemCount) -> setEmptyState(
                        emptyItems = stateItems.empty,
                    )
                    it.isErrorState() -> setErrorState(
                        baseError = it.refresh.cast<LoadState.Error>()?.error,
                        baseErrorItems = stateItems.error,
                    )
                    it.isIdleState(itemCount) -> setIdleState(
                        pagingRecyclerView = recyclerView,
                    )
                    it.isLoadingState() -> setLoadingState(
                        loadingItems = stateItems.loading,
                    )
                }
            }
    }

    @FlowPreview
    suspend fun <T : Any, ViewHolder : RecyclerView.ViewHolder> bind(
        loadState: MutableSharedFlow<LoadState>? = null,
        pagingAdapter: PagingDataAdapter<T, ViewHolder>,
        pagingRecyclerView: RecyclerView,
        type: LoadStateRefreshItemsType,
    ) {
        updateStateItems(type)
        flowOf(loadState?.toCombinedLoadStates(), pagingAdapter.loadStateFlow)
            .filterNotNull()
            .flattenMerge()
            .distinctUntilChanged()
            .collectLatest {
                val itemCount = pagingAdapter.itemCount
                when {
                    it.isEmptyState(itemCount) -> setEmptyState(
                        emptyItems = stateItems.empty,
                    )
                    it.isErrorState() -> setErrorState(
                        baseAction = { pagingAdapter.refresh() },
                        baseError = it.refresh.cast<LoadState.Error>()?.error,
                        baseErrorItems = stateItems.error,
                    )
                    it.isIdleState(itemCount) -> setIdleState(
                        pagingRecyclerView = pagingRecyclerView,
                    )
                    it.isLoadingState() -> setLoadingState(
                        loadingItems = stateItems.loading,
                    )
                }
            }
    }

    private fun CombinedLoadStates.isEmptyState(itemCount: Int) =
        refresh is LoadState.NotLoading
            && append is LoadState.NotLoading
            && append.endOfPaginationReached
            && itemCount < 1

    private fun CombinedLoadStates.isErrorState() =
        refresh is LoadState.Error

    private fun CombinedLoadStates.isIdleState(itemCount: Int) =
        refresh is LoadState.NotLoading
            && itemCount > 0

    private fun CombinedLoadStates.isLoadingState() =
        refresh is LoadState.Loading

    private fun MutableSharedFlow<LoadState>.toCombinedLoadStates() = map { refresh ->
        val notLoading = LoadState.NotLoading(endOfPaginationReached = true)
        val source = LoadStates(append = notLoading, prepend = notLoading, refresh = notLoading)
        CombinedLoadStates(
            append = notLoading,
            prepend = notLoading,
            refresh = refresh,
            source = source,
        )
    }

    private fun setEmptyState(emptyItems: List<CastcleViewEntity>) {
        isRefreshing = false
        binding.recyclerView.adapter
            ?.cast<CastcleAdapter>()
            ?.submitList(emptyItems)
        visible()
    }

    private fun setErrorState(
        baseAction: () -> Unit = {},
        baseError: Throwable?,
        baseErrorItems: List<CastcleViewEntity>,
    ) {
        val retryException = baseError?.cast<RetryException>()
        val action = retryException?.action ?: baseAction
        val error = retryException?.error ?: baseError
        val errorItems = retryException?.errorItems ?: baseErrorItems
        val errorItemsWithAction = errorItems
            .filterIsInstance<LoadStateRefreshViewEntity>()
            .onEach { it.updateItem(updateAction = action, updateError = error) }
        binding.recyclerView.adapter?.cast<CastcleAdapter>()?.submitList(errorItemsWithAction)
        isRefreshing = false
        visible()
    }

    private suspend fun setIdleState(pagingRecyclerView: RecyclerView) {
        if (isVisible && isRefreshing) {
            repeat(15) {
                delay(10)
                pagingRecyclerView.scrollToPosition(0)
            }
            isRefreshing = false
        }
        gone()
    }

    private fun setLoadingState(loadingItems: List<CastcleViewEntity>) {
        isRefreshing = true
        binding.recyclerView.adapter
            ?.cast<CastcleAdapter>()
            ?.submitList(loadingItems)
        visible()
    }

    private fun updateStateItems(type: LoadStateRefreshItemsType) {
        stateItems = when (type) {
            CONTENT -> StateItems(
                empty = ErrorStateViewEntity.create(1),
                error = ErrorStateViewEntity.create(1),
                loading = LoadingStateCastViewEntity.create(1)
                    .plus(LoadingStateCommentViewEntity.create(5)),
            )
            FEED -> StateItems(
                empty = EmptyStateFeedViewEntity.create(1),
                error = EmptyStateFeedViewEntity.create(1),
                loading = LoadingStateCastViewEntity.create(3),
            )
            PROFILE -> StateItems(
                empty = ErrorStateViewEntity.create(1),
                error = ErrorStateViewEntity.create(1),
                loading = LoadingStateProfileViewEntity.create(1)
                    .plus(LoadingStateCastViewEntity.create(1)),
            )
            SEARCH_CAST -> StateItems(
                empty = EmptyStateSearchViewEntity.create(1),
                error = ErrorStateViewEntity.create(1),
                loading = LoadingStateCastViewEntity.create(3),
            )
            SEARCH_USER, WHO_TO_FOLLOW -> StateItems(
                empty = EmptyStateSearchViewEntity.create(1),
                error = ErrorStateViewEntity.create(1),
                loading = LoadingStateUserViewEntity.create(10),
            )
            WALLET_DASHBOARD -> StateItems(
                empty = ErrorStateViewEntity.create(1),
                error = ErrorStateViewEntity.create(1),
                loading = LoadingViewEntity.create(1),
            )
        }
    }

    data class StateItems(
        val empty: List<CastcleViewEntity> = listOf(),
        val error: List<CastcleViewEntity> = listOf(),
        val loading: List<CastcleViewEntity> = listOf(),
    )

}