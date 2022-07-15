package com.castcle.android.presentation.feed.item_feed_recast

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemFeedRecastBinding
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image_1.FeedImage1ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_2.FeedImage2ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_3.FeedImage3ViewRenderer
import com.castcle.android.presentation.feed.item_feed_image_4.FeedImage4ViewRenderer
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewRenderer
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import io.reactivex.disposables.CompositeDisposable

class FeedRecastViewHolder(
    private val binding: ItemFeedRecastBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedRecastViewEntity>(binding.root) {

    override var item = FeedRecastViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(FeedImage1ViewRenderer(CastType.Recast))
            registerRenderer(FeedImage2ViewRenderer(CastType.Recast))
            registerRenderer(FeedImage3ViewRenderer(CastType.Recast))
            registerRenderer(FeedImage4ViewRenderer(CastType.Recast))
            registerRenderer(FeedNewCastViewRenderer())
            registerRenderer(FeedQuoteViewRenderer())
            registerRenderer(FeedRecastViewRenderer())
            registerRenderer(FeedTextViewRenderer(CastType.Recast))
            registerRenderer(FeedWebViewRenderer(CastType.Recast))
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun bind(bindItem: FeedRecastViewEntity) {
        adapter.submitList(item.reference)
        binding.tvRecast.text = if (item.user.isOwner && item.user.type is UserType.People) {
            string(R.string.you_recasted)
        } else {
            context().getString(R.string.user_recasted, item.user.displayName)
        }
    }

}