package com.castcle.android.presentation.feed.item_feed_who_to_follow

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedWhoToFollowBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedWhoToFollowViewRenderer : CastcleViewRenderer<FeedWhoToFollowViewEntity,
    FeedWhoToFollowViewHolder,
    FeedListener>(R.layout.item_feed_who_to_follow) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedWhoToFollowViewHolder(
        ItemFeedWhoToFollowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}