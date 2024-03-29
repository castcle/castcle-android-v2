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

package com.castcle.android.presentation.content.content

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastclePagingDataAdapter
import com.castcle.android.core.custom_view.load_state.*
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.custom_view.mention_view.MentionView
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentContentBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.content.content.item_comment.CommentViewRenderer
import com.castcle.android.presentation.content.content.item_content_metrics.ContentMetricsViewRenderer
import com.castcle.android.presentation.content.content.item_reply.ReplyViewRenderer
import com.castcle.android.presentation.content.content_metrics.ContentMetricsType
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
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("SetTextI18n")
class ContentFragment : BaseFragment(), LoadStateListener, FeedListener, ContentListener {

    private val viewModel by stateViewModel<ContentViewModel> { parametersOf(args.contentId) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val directions = ContentFragmentDirections

    private val args by navArgs<ContentFragmentArgs>()

    override fun initViewProperties() {
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadStateAppendAdapter(compositeDisposable, this)
        )
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = args.contentOwnerDisplayName?.let { getString(R.string.cast_of, it) },
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collectLatest {
                binding.ivAvatar.loadAvatarImage(it.avatar.thumbnail)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun initListener() {
        compositeDisposable += binding.clComment.onClick {
            binding.etComment.showKeyboard()
        }
        compositeDisposable += binding.ivSentComment.onClick {
            if (binding.etComment.text.isNotBlank()) {
                showLoading()
                viewModel.sentComment(binding.etComment.text.toString())
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.fetchContent()
            adapter.refresh()
            clearComment()
        }
        binding.etComment.setMentionEnabled(true)
        binding.etComment.setMentionTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                viewModel.getMentions(text.toString())
            }
        })
        binding.etComment.setHashtagEnabled(true)
        binding.etComment.setHashtagTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                viewModel.getHashtag(text.toString())
            }
        })
        setFragmentResultListener(REPLY_RESULT) { key, bundle ->
            if (key == REPLY_RESULT) {
                when (val result = bundle.getParcelable<Parcelable>(REPLY_RESULT)) {
                    is OptionDialogType.MyCommentOption -> onReplyClicked(
                        castcleId = result.castcleId,
                        commentId = result.commentId,
                    )
                    is OptionDialogType.OtherCommentOption -> onReplyClicked(
                        castcleId = result.castcleId,
                        commentId = result.commentId,
                    )
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.mentions.observe(viewLifecycleOwner) {
            binding.etComment.updateMentionsItems(it)
        }
        viewModel.hashtags.observe(viewLifecycleOwner) {
            binding.etComment.updateHashtagItems(it)
        }
        viewModel.contentOwnerDisplayName.observe(viewLifecycleOwner) {
            binding.actionBar.bind(
                leftButtonAction = { backPress() },
                title = getString(R.string.cast_of, it),
            )
        }
        viewModel.onCommentSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            clearComment()
        }
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
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
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                pagingAdapter = adapter,
                pagingRecyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.CONTENT,
            )
        }
    }

    private fun clearComment() {
        hideKeyboard()
        binding.etComment.setText("")
        binding.etComment.clearFocus()
        viewModel.targetCommentId.value = null
    }

    override fun onCommentClicked(cast: CastEntity, user: UserEntity) {
        binding.etComment.showKeyboard()
    }

    override fun onContentMetricsClicked(type: ContentMetricsType) {
        directions.toContentMetricsDialogFragment(type).navigate()
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

    override fun onLikeCommentClicked(comment: CommentEntity) {
        viewModel.likeComment(comment)
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

    override fun onQuoteCastCountClicked(contentId: String) {
        directions.toContentQuoteCastFragment(contentId = contentId).navigate()
    }

    override fun onRecastClicked(cast: CastEntity) {
        shareViewModel.isUserCanEngagement(
            isGuestAction = { directions.toLoginFragment().navigate() },
            isMemberAction = { directions.toRecastDialogFragment(contentId = cast.id).navigate() },
            isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
        )
    }

    override fun onReplyClicked(castcleId: String, commentId: String) {
        viewModel.targetCommentId.value = commentId
        binding.etComment.setText("$castcleId ")
        binding.etComment.setSelection(binding.etComment.length())
        lifecycleScope.launch {
            while (!isKeyboardVisible(binding)) {
                binding.etComment.showKeyboard()
                delay(50)
            }
        }
    }

    override fun onUserClicked(user: UserEntity) {
        directions.toProfileFragment(user).navigate()
    }

    override fun onViewReportClicked(id: String, ignoreReportContentId: List<String>) {
        viewModel.showReportingContent(id = id, ignoreReportContentId = ignoreReportContentId)
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchContent()
        changeSoftInputMode(true)
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(CommentViewRenderer())
            registerRenderer(ContentMetricsViewRenderer())
            registerRenderer(FeedImageViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedReportViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(FeedWebImageViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), isDefaultItem = true)
            registerRenderer(ReplyViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentContentBinding.inflate(layoutInflater)
    }

    companion object {
        const val REPLY_RESULT = "REPLY_RESULT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}