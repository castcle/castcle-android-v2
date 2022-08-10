package com.castcle.android.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.extensions.setRefreshColor
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewRenderer
import com.castcle.android.presentation.feed.item_feed_reporting.FeedReportingViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.home.HomeViewModel
import com.castcle.android.presentation.profile.item_profile_page.ProfilePageViewRenderer
import com.castcle.android.presentation.profile.item_profile_user.ProfileUserViewRenderer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : BaseFragment(), LoadStateListener, FeedListener, ProfileListener {

    private val viewModel by stateViewModel<ProfileViewModel> { parametersOf(args.user.id) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val args by navArgs<ProfileFragmentArgs>()

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
    }

    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitData)
        }
        lifecycleScope.launch {
            viewModel.currentUser.collectLatest {
                binding.actionBar.bind(
                    leftButtonAction = { backPress() },
                    title = it?.displayName ?: args.user.displayName,
                )
            }
        }
        lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                pagingAdapter = adapter,
                pagingRecyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.PROFILE,
            )
        }
    }

    override fun onCommentClicked(cast: CastEntity, user: UserEntity) {
        ProfileFragmentDirections.toContentFragment(cast.id, user.displayName).navigate()
    }

    override fun onContentOptionClicked(cast: CastEntity, user: UserEntity) {
        val optionType = if (cast.isOwner) {
            OptionDialogType.MyContentOption(contentId = cast.id)
        } else {
            OptionDialogType.OtherContentOption(contentId = cast.id)
        }
        ProfileFragmentDirections.toOptionDialogFragment(optionType).navigate()
    }

    override fun onFollowClicked(user: UserEntity) {
        shareViewModel.followUser(targetUser = user)
    }

    override fun onFollowingFollowersClicked(isFollowing: Boolean, user: UserEntity) {
        ProfileFragmentDirections.toFollowingFollowersFragment(isFollowing, user.id).navigate()
    }

    override fun onLikeClicked(cast: CastEntity) {
        shareViewModel.likeCasts(targetCasts = cast)
    }

    override fun onNewCastClicked(userId: String) {
        ProfileFragmentDirections.toNewCastFragment(quoteCastId = null, userId = userId).navigate()
    }

    override fun onOptionClicked(type: OptionDialogType) {
        ProfileFragmentDirections.toOptionDialogFragment(type).navigate()
    }

    override fun onRecastClicked(cast: CastEntity) {
        ProfileFragmentDirections.toRecastDialogFragment(contentId = cast.id).navigate()
    }

    override fun onRefreshClicked() {
        adapter.refresh()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onUserClicked(user: UserEntity) {
        ProfileFragmentDirections.toProfileFragment(user).navigate()
    }

    override fun onViewReportingClicked(contentId: List<String>) {
        shareViewModel.showReportingContent(contentId = contentId)
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
            registerRenderer(FeedImageViewRenderer())
            registerRenderer(FeedNewCastViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedReportingViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), isDefaultItem = true)
            registerRenderer(ProfilePageViewRenderer())
            registerRenderer(ProfileUserViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}