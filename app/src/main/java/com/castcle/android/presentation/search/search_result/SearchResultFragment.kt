package com.castcle.android.presentation.search.search_result

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_state_user.LoadingStateUserViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewRenderer
import com.castcle.android.presentation.feed.item_feed_report.FeedReportViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import com.castcle.android.presentation.home.HomeViewModel
import com.castcle.android.presentation.search.search.SearchFragmentDirections
import com.castcle.android.presentation.search.search_result.item_search_people.SearchPeopleViewRenderer
import com.castcle.android.presentation.who_to_follow.WhoToFollowListener
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class SearchResultFragment : BaseFragment(), FeedListener, LoadStateListener, WhoToFollowListener {

    private val viewModel by stateViewModel<SearchResultViewModel> { parametersOf(sessionId, type) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val directions = SearchFragmentDirections

    @FlowPreview
    override fun initViewProperties() {
        binding.actionBar.gone()
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadStateAppendAdapter(compositeDisposable, this)
        )
        viewLifecycleOwner.lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                pagingAdapter = adapter,
                pagingRecyclerView = binding.recyclerView,
                type = if (type is SearchType.People) {
                    LoadStateRefreshItemsType.SEARCH_USER
                } else {
                    LoadStateRefreshItemsType.SEARCH_CAST
                },
            )
        }
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.keyword.collectLatest { adapter.refresh() }
        }
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
    }

    override fun onCommentClicked(cast: CastEntity, user: UserEntity) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toContentFragment(cast.id, user.displayName).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onFollowClicked(user: UserEntity) {
        shareViewModel.followUser(
            isGuestAction = { directions.toLoginFragment().navigate() },
            targetUser = user,
        )
    }

    override fun onHashtagClicked(keyword: String) {
        directions.toSearchFragment(keyword).navigate()
    }

    override fun onImageClicked(image: List<ImageEntity>, position: Int) {
        StfalconImageViewer.Builder(context, image, ::loadViewLargeImage)
            .withStartPosition(position)
            .withHiddenStatusBar(true)
            .allowSwipeToDismiss(true)
            .allowZooming(true)
            .show()
    }

    override fun onLikeClicked(cast: CastEntity) {
        shareViewModel.likeCast(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
            targetCast = cast,
        )
    }

    override fun onLinkClicked(url: String) {
        openUrl(url)
    }

    override fun onMentionClicked(castcleId: String) {
        directions.toProfileFragment(UserEntity(id = castcleId)).navigate()
    }

    override fun onOptionClicked(type: OptionDialogType) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toOptionDialogFragment(type).navigate() },
        )
    }

    override fun onRecastClicked(cast: CastEntity) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toRecastDialogFragment(contentId = cast.id).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onUserClicked(user: UserEntity) {
        hideKeyboard()
        directions.toProfileFragment(user).navigate()
    }

    override fun onRefreshClicked() {
        adapter.refresh()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onViewReportClicked(id: String, ignoreReportContentId: List<String>) {
        viewModel.showReportingContent(id = id, ignoreReportContentId = ignoreReportContentId)
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onStop()
    }

    override fun onStart() {
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        super.onStart()
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedReportViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(FeedWebImageViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), type !is SearchType.People)
            registerRenderer(LoadingStateUserViewRenderer(), type is SearchType.People)
            registerRenderer(SearchPeopleViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    private val sessionId by lazy {
        arguments?.getLong(SESSION_ID) ?: System.currentTimeMillis()
    }

    private val type by lazy {
        arguments?.getParcelable<SearchType>(TYPE) ?: SearchType.Trend
    }

    companion object {
        private const val SESSION_ID = "SESSION_ID"
        private const val TYPE = "TYPE"
        fun newInstance(sessionId: Long, type: SearchType) = SearchResultFragment().apply {
            arguments = Bundle().apply {
                putLong(SESSION_ID, sessionId)
                putParcelable(TYPE, type)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}