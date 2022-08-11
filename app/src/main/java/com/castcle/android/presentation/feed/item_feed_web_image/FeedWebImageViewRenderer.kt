package com.castcle.android.presentation.feed.item_feed_web_image

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedWebImageBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedWebImageViewRenderer(
    private val displayType: FeedDisplayType = FeedDisplayType.Normal
) : CastcleViewRenderer<FeedWebImageViewEntity,
    FeedWebImageViewHolder,
    FeedListener>(R.layout.item_feed_web_image) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedWebImageViewHolder(
        ItemFeedWebImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, displayType
    )

}