package com.castcle.android.presentation.feed.item_feed_web

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedWebBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedWebViewRenderer(
    private val displayType: FeedDisplayType = FeedDisplayType.Normal
) : CastcleViewRenderer<FeedWebViewEntity,
    FeedWebViewHolder,
    FeedListener>(R.layout.item_feed_web) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedWebViewHolder(
        ItemFeedWebBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, displayType
    )

}