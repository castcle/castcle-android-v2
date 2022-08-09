package com.castcle.android.presentation.feed.item_feed_quote

import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.CastcleTextView
import com.castcle.android.core.custom_view.LinkedType
import com.castcle.android.core.custom_view.participate_bar.ParticipateBarListener
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemFeedQuoteBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedQuoteViewHolder(
    private val binding: ItemFeedQuoteBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedQuoteViewEntity>(binding.root), UserBarListener, ParticipateBarListener {

    override var item = FeedQuoteViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer(FeedDisplayType.QuoteCast))
            registerRenderer(FeedTextViewRenderer(FeedDisplayType.QuoteCast))
            registerRenderer(FeedWebViewRenderer(FeedDisplayType.QuoteCast))
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        compositeDisposable += binding.castcleTextView.onClick {
            binding.castcleTextView.toggle()
        }
        binding.castcleTextView.setLinkClickListener(object : CastcleTextView.LinkClickListener {
            override fun onLinkClicked(linkType: LinkedType, matchedText: String) {
                if (linkType == LinkedType.URL) {
                    listener.onLinkClicked(matchedText)
                } else if (linkType == LinkedType.HASHTAG) {
                    listener.onHashtagClicked(matchedText)
                }
            }
        })
    }

    override fun bind(bindItem: FeedQuoteViewEntity) {
        adapter.submitList(item.reference)
        binding.participateBar.bind(item.cast, this)
        binding.userBar.bind(item.cast, item.user, this, true)
        binding.castcleTextView.onClearMessage()
        if (item.cast.type is CastType.Long) {
            binding.castcleTextView.setCollapseText(item.cast.message)
        } else {
            binding.castcleTextView.appendLinkText(item.cast.message)
        }
    }

    override fun onCommentClicked(cast: CastEntity) {
        listener.onCommentClicked(cast, item.user)
    }

    override fun onFollowClicked(user: UserEntity) {
        listener.onFollowClicked(user)
    }

    override fun onLikeClicked(cast: CastEntity) {
        listener.onLikeClicked(cast)
    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {
        listener.onContentOptionClicked(cast, user)
    }

    override fun onRecastClicked(cast: CastEntity) {
        listener.onRecastClicked(item.referenceCast ?: item.cast)
    }

    override fun onUserClicked(user: UserEntity) {
        listener.onUserClicked(user)
    }

}