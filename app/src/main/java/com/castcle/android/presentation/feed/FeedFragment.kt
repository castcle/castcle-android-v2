package com.castcle.android.presentation.feed

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.extensions.openUrl
import com.castcle.android.core.extensions.setRefreshColor
import com.castcle.android.databinding.FragmentFeedBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image_1.FeedImage1ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_2.FeedImage2ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_3.FeedImage3ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_4.FeedImage4ViewRenderer
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_who_to_follow.FeedWhoToFollowViewRenderer
import com.castcle.android.presentation.home.HomeFragmentDirections
import com.castcle.android.presentation.home.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class FeedFragment : BaseFragment(), FeedListener, LoadStateListener {

    private val viewModel by stateViewModel<FeedViewModel>()

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    @FlowPreview
    override fun initViewProperties() {
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
                type = LoadStateRefreshItemsType.FEED,
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isGuest.collectLatest { isGuest ->
                if (isGuest) {
                    binding.actionBar.bind(
                        leftButtonIcon = R.drawable.ic_castcle,
                        leftButtonAction = { scrollToTop() },
                        rightButtonAction = { HomeFragmentDirections.toLoginFragment().navigate() },
                        rightButtonIcon = R.drawable.ic_user,
                        title = R.string.for_you,
                        titleColor = R.color.blue,
                    )
                } else {
                    binding.actionBar.bind(
                        leftButtonIcon = R.drawable.ic_castcle,
                        leftButtonAction = { scrollToTop() },
                        rightButtonAction = {
                            HomeFragmentDirections.toSettingFragment().navigate()
                        },
                        rightButtonIcon = R.drawable.ic_hamburger,
                        rightSecondButtonAction = {},
                        rightSecondButtonIcon = R.drawable.ic_wallet,
                        title = R.string.for_you,
                        titleColor = R.color.blue,
                    )
                }
            }
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
    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
    }

    fun scrollToTop() {
        binding.recyclerView.scrollToPosition(0)
    }

    override fun onCommentClicked(cast: CastEntity, user: UserEntity) {
        HomeFragmentDirections.toContentFragment(cast.id, user.displayName).navigate()
    }

    override fun onFollowClicked(user: UserEntity) {
        shareViewModel.followUser(targetUser = user)
    }

    override fun onHashtagClicked(keyword: String) {

    }

    override fun onImageClicked(photo: ImageEntity) {
        openUrl(photo.original)
    }

    override fun onLikeClicked(cast: CastEntity) {
        shareViewModel.likeCasts(targetCasts = cast)
    }

    override fun onLinkClicked(url: String) {
        openUrl(url)
    }

    override fun onNewCastClicked() {

    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {

    }

    override fun onRecastClicked(cast: CastEntity) {

    }

    override fun onUserClicked(user: UserEntity) {
        HomeFragmentDirections.toProfileFragment(user).navigate()
    }

    override fun onWhoToFollowClicked() {
        HomeFragmentDirections.toWhoToFollowFragment().navigate()
    }

    override fun onRefreshClicked() {
        adapter.refresh()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onPause() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onPause()
    }

    override fun onResume() {
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        super.onResume()
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(FeedImage1ViewRenderer())
            registerRenderer(FeedImage2ViewRenderer())
            registerRenderer(FeedImage3ViewRenderer())
            registerRenderer(FeedImage4ViewRenderer())
            registerRenderer(FeedNewCastViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(FeedWhoToFollowViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), isDefaultItem = true)
        }
    }

    private val binding by lazy {
        FragmentFeedBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance() = FeedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}