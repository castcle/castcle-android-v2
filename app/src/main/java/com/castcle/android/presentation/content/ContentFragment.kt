package com.castcle.android.presentation.content

import android.annotation.SuppressLint
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
import com.castcle.android.core.custom_view.mention_view.MentionView
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentContentBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.content.item_comment.CommentViewRenderer
import com.castcle.android.presentation.content.item_content_metrics.ContentMetricsViewRenderer
import com.castcle.android.presentation.content.item_reply.ReplyViewRenderer
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.home.HomeViewModel
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class ContentFragment : BaseFragment(), LoadStateListener, FeedListener, ContentListener {

    private val viewModel by stateViewModel<ContentViewModel> { parametersOf(args.contentId) }

    private val shareViewModel by sharedViewModel<HomeViewModel>()

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mentions.collectLatest {
                binding.etComment.updateMentionsItems(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hashtags.collectLatest {
                binding.etComment.updateHashtagItems(it)
            }
        }
    }

    override fun initListener() {
        compositeDisposable += binding.ivSentComment.onClick {
            if (binding.etComment.text.isNotBlank()) {
                showLoading()
                viewModel.sentComment(binding.etComment.text.toString())
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
            clearComment()
        }
        binding.etComment.setMentionEnabled(true)
        binding.etComment.setMentionTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                if (text.isNotBlank() && text != "@") {
                    viewModel.getMentions(text.toString())
                }
            }
        })
        binding.etComment.setHashtagEnabled(true)
        binding.etComment.setHashtagTextChangedListener(object : MentionView.OnChangedListener {
            override fun onChanged(view: MentionView, text: CharSequence) {
                if (text.isNotBlank() && text != "#") {
                    viewModel.getHashtag(text.toString())
                }
            }
        })
    }

    override fun initObserver() {
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

    override fun onLikeClicked(cast: CastEntity) {
        shareViewModel.likeCasts(cast)
    }

    override fun onLikeCommentClicked(comment: CommentEntity) {
        viewModel.likeComment(comment)
    }

    override fun onLikeCountClicked(contentId: String, hasRecast: Boolean) {

    }

    override fun onQuoteCastCountClicked(contentId: String) {

    }

    override fun onRecastClicked(cast: CastEntity) {
        ContentFragmentDirections.toRecastDialogFragment(cast.id).navigate()
    }

    override fun onRecastCountClicked(contentId: String, hasLike: Boolean) {

    }

    @SuppressLint("SetTextI18n")
    override fun onReplyClicked(castcleId: String, commentId: String) {
        viewModel.targetCommentId.value = commentId
        binding.etComment.setText("@$castcleId ")
        binding.etComment.setSelection(binding.etComment.length())
        binding.etComment.showKeyboard()
    }

    override fun onUserClicked(user: UserEntity) {
        ContentFragmentDirections.toProfileFragment(user).navigate()
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
        changeSoftInputMode(true)
    }

    private val adapter by lazy {
        CastclePagingDataAdapter(this, compositeDisposable).apply {
            registerRenderer(CommentViewRenderer())
            registerRenderer(ContentMetricsViewRenderer())
            registerRenderer(FeedImageViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedTextViewRenderer())
            registerRenderer(FeedWebViewRenderer())
            registerRenderer(LoadingStateCastViewRenderer(), isDefaultItem = true)
            registerRenderer(ReplyViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentContentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}