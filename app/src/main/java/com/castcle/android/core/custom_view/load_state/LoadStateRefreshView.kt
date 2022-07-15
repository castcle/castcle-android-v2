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
import com.castcle.android.core.custom_view.load_state.item_empty_state_feed.EmptyStateFeedViewEntity
import com.castcle.android.core.custom_view.load_state.item_empty_state_feed.EmptyStateFeedViewRenderer
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewEntity
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_profile.LoadingStateProfileViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading_state_profile.LoadingStateProfileViewRenderer
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
            registerRenderer(EmptyStateFeedViewRenderer())
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer())
            registerRenderer(LoadingStateProfileViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutLoadStateRefreshBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val compositeDisposable = CompositeDisposable()

    private var isRefreshing = false

    private var stateItems: Triple<List<CastcleViewEntity>, List<CastcleViewEntity>, List<CastcleViewEntity>> =
        Triple(
            EmptyStateFeedViewEntity.create(1),
            EmptyStateFeedViewEntity.create(1),
            LoadingStateCastViewEntity.create(3),
        )

    init {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
    }

    @FlowPreview
    suspend fun <T : Any, ViewHolder : RecyclerView.ViewHolder> bind(
        loadState: MutableStateFlow<LoadState>? = null,
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
                        emptyItems = stateItems.first,
                    )
                    it.isErrorState() -> setErrorState(
                        error = it.refresh.cast<LoadState.Error>()?.error,
                        errorItems = stateItems.second,
                        refreshAction = { pagingAdapter.refresh() },
                        retryAction = { pagingAdapter.retry() },
                    )
                    it.isIdleState(itemCount) -> setIdleState(
                        pagingRecyclerView = pagingRecyclerView,
                    )
                    it.isLoadingState() -> setLoadingState(
                        loadingItems = stateItems.third,
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

    private fun MutableStateFlow<LoadState>.toCombinedLoadStates() = map { refresh ->
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
        error: Throwable?,
        errorItems: List<CastcleViewEntity>,
        refreshAction: () -> Unit,
        retryAction: () -> Unit,
    ) {
        isRefreshing = false
        binding.recyclerView.adapter
            ?.cast<CastcleAdapter>()
            ?.submitList(
                errorItems
                    .filterIsInstance<LoadStateRefreshViewEntity>()
                    .onEach { it.error(refreshAction, retryAction, error) }
            )
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
            LoadStateRefreshItemsType.FEED -> Triple(
                EmptyStateFeedViewEntity.create(1),
                EmptyStateFeedViewEntity.create(1),
                LoadingStateCastViewEntity.create(3),
            )
            LoadStateRefreshItemsType.PROFILE -> Triple(
                ErrorStateViewEntity.create(1),
                ErrorStateViewEntity.create(1),
                LoadingStateProfileViewEntity.create(1).plus(LoadingStateCastViewEntity.create(1)),
            )
            LoadStateRefreshItemsType.WHO_TO_FOLLOW -> Triple(
                ErrorStateViewEntity.create(1),
                ErrorStateViewEntity.create(1),
                LoadingStateCastViewEntity.create(3),
            )
        }
    }

}