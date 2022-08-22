package com.castcle.android.presentation.feed.item_feed_report

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedReportBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedReportViewRenderer(
    private val displayType: FeedDisplayType = FeedDisplayType.Normal
) : CastcleViewRenderer<FeedReportViewEntity,
    FeedReportViewHolder,
    FeedListener>(R.layout.item_feed_report) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedReportViewHolder(
        ItemFeedReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, displayType
    )

}