/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.content.content_quote_cast

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
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
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class ContentQuoteCastFragment : BaseFragment(), FeedListener, LoadStateListener {

    private val viewModel by stateViewModel<ContentQuoteCastViewModel> { parametersOf(args.contentId) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val args by navArgs<ContentQuoteCastFragmentArgs>()

    private val directions = ContentQuoteCastFragmentDirections

    @FlowPreview
    override fun initViewProperties() {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = getString(R.string.quote_cast),
        )
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
                type = LoadStateRefreshItemsType.SEARCH_CAST,
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
            registerRenderer(LoadingStateCastViewRenderer(), true)
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

}