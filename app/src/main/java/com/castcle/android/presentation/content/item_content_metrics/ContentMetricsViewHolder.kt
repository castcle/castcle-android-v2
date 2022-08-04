package com.castcle.android.presentation.content.item_content_metrics

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemContentMetricsBinding
import com.castcle.android.presentation.content.ContentListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ContentMetricsViewHolder(
    private val binding: ItemContentMetricsBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ContentListener,
) : CastcleViewHolder<ContentMetricsViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.tvLikeCount.onClick {
            listener.onLikeCountClicked(
                contentId = item.contentId,
                hasRecast = item.recastCount > 0,
            )
        }
        compositeDisposable += binding.tvRecastCount.onClick {
            listener.onRecastCountClicked(
                contentId = item.contentId,
                hasLike = item.likeCount > 0,
            )
        }
        compositeDisposable += binding.tvQuoteCastCount.onClick {
            listener.onQuoteCastCountClicked(contentId = item.contentId)
        }
    }

    override var item = ContentMetricsViewEntity()

    override fun bind(bindItem: ContentMetricsViewEntity) {
        binding.tvLikeCount.isVisible = item.likeCount > 0
        binding.tvLikeCount.text = context().getString(
            R.string.like_count,
            item.likeCount.asComma()
        )
        binding.tvRecastCount.isVisible = item.recastCount > 0
        binding.tvRecastCount.text = context().getString(
            R.string.recast_count,
            item.recastCount.asComma()
        )
        binding.tvQuoteCastCount.isVisible = item.quoteCount > 0
        binding.tvQuoteCastCount.text = context().getString(
            R.string.quote_cast_count,
            item.quoteCount.asComma()
        )
    }

}