package com.castcle.android.presentation.feed.item_feed_image_item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedImageItemBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedImageItemViewRenderer : CastcleViewRenderer<FeedImageItemViewEntity,
    FeedImageItemViewHolder,
    FeedListener>(R.layout.item_feed_image_item) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedImageItemViewHolder(
        ItemFeedImageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}