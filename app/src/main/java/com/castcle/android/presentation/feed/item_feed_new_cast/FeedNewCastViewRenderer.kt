package com.castcle.android.presentation.feed.item_feed_new_cast

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedNewCastBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedNewCastViewRenderer : CastcleViewRenderer<FeedNewCastViewEntity,
    FeedNewCastViewHolder,
    FeedListener>(R.layout.item_feed_new_cast) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedNewCastViewHolder(
        ItemFeedNewCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}