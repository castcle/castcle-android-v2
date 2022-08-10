package com.castcle.android.presentation.feed.item_feed_reporting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedReportingBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedReportingViewRenderer : CastcleViewRenderer<FeedReportingViewEntity,
    FeedReportingViewHolder,
    FeedListener>(R.layout.item_feed_reporting) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedReportingViewHolder(
        ItemFeedReportingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}