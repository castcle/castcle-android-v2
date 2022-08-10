package com.castcle.android.presentation.feed.item_feed_reporting

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemFeedReportingBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedReportingViewHolder(
    binding: ItemFeedReportingBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedReportingViewEntity>(binding.root) {

    override var item = FeedReportingViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            val contentId = listOf(
                item.reportingContentId?.ifBlank { null },
                item.reportingReferenceContentId?.ifBlank { null },
            )
            listener.onViewReportingClicked(contentId = contentId.filterNotNull())
        }
    }

}