package com.castcle.android.presentation.feed.item_feed_image

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedImageBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedImageViewRenderer(
    private val displayType: FeedDisplayType = FeedDisplayType.Normal
) : CastcleViewRenderer<FeedImageViewEntity,
    FeedImageViewHolder,
    FeedListener>(R.layout.item_feed_image) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedImageViewHolder(
        ItemFeedImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, displayType
    )

}