package com.castcle.android.presentation.search.top_trends.item_top_trends_item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemTopTrendsItemBinding
import com.castcle.android.presentation.search.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable

class TopTrendsItemViewRenderer : CastcleViewRenderer<TopTrendsItemViewEntity,
    TopTrendsItemViewHolder,
    TopTrendsListener>(R.layout.item_top_trends_item) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: TopTrendsListener,
        compositeDisposable: CompositeDisposable
    ) = TopTrendsItemViewHolder(
        ItemTopTrendsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}