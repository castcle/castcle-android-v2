package com.castcle.android.presentation.feed.item_feed_report

import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedReportBinding
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedReportViewHolder(
    private val binding: ItemFeedReportBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
    private val displayType: FeedDisplayType,
) : CastcleViewHolder<FeedReportViewEntity>(binding.root) {

    override var item = FeedReportViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onViewReportClicked(
                id = item.uniqueId,
                ignoreReportContentId = item.ignoreReportContentId,
            )
        }
    }

    override fun bind(bindItem: FeedReportViewEntity) {
        val marginBottom = if (
            displayType is FeedDisplayType.QuoteCast || displayType is FeedDisplayType.Recast
        ) {
            0
        } else {
            dimenPx(com.intuit.sdp.R.dimen._4sdp)
        }
        binding.root.layoutParams = binding.root.layoutParams.cast<RecyclerView.LayoutParams>()
            ?.apply { setMargins(0, 0, 0, marginBottom) }
        if (displayType is FeedDisplayType.NewCast) {
            binding.root.setBackgroundColor(color(R.color.transparent))
            binding.root.background = drawable(R.drawable.bg_outline_corner_16dp)
            binding.root.backgroundTintList = colorStateList(R.color.gray_1)
        } else {
            binding.root.setBackgroundColor(color(R.color.black_background_2))
        }
    }

}