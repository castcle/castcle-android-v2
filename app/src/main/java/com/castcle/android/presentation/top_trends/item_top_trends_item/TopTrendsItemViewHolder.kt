package com.castcle.android.presentation.top_trends.item_top_trends_item

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemTopTrendsItemBinding
import com.castcle.android.presentation.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class TopTrendsItemViewHolder(
    private val binding: ItemTopTrendsItemBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: TopTrendsListener,
) : CastcleViewHolder<TopTrendsItemViewEntity>(binding.root) {

    override var item = TopTrendsItemViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onTrendClicked(item.trend.slug)
        }
    }

    override fun bind(bindItem: TopTrendsItemViewEntity) {
        binding.tvName.text = item.trend.name
        binding.tvRank.text = context().getString(R.string.rank_trending, item.trend.rank)
        binding.tvCount.text = context().getString(R.string.cast_count, item.trend.count.asCount())
    }

}