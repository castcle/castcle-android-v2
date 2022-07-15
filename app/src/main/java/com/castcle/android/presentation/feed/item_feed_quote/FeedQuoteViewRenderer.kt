package com.castcle.android.presentation.feed.item_feed_quote

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedQuoteBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedQuoteViewRenderer : CastcleViewRenderer<FeedQuoteViewEntity,
    FeedQuoteViewHolder,
    FeedListener>(R.layout.item_feed_quote) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable,
    ) = FeedQuoteViewHolder(
        ItemFeedQuoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}