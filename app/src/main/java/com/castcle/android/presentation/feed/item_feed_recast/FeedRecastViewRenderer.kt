package com.castcle.android.presentation.feed.item_feed_recast

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedRecastBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedRecastViewRenderer : CastcleViewRenderer<FeedRecastViewEntity,
    FeedRecastViewHolder,
    FeedListener>(R.layout.item_feed_recast) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable,
    ) = FeedRecastViewHolder(
        ItemFeedRecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}