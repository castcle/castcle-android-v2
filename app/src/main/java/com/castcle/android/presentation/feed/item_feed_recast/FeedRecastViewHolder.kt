package com.castcle.android.presentation.feed.item_feed_recast

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemFeedRecastBinding
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import io.reactivex.disposables.CompositeDisposable

class FeedRecastViewHolder(
    private val binding: ItemFeedRecastBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedRecastViewEntity>(binding.root) {

    override var item = FeedRecastViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedTextViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedWebViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedWebImageViewRenderer(FeedDisplayType.Recast))
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