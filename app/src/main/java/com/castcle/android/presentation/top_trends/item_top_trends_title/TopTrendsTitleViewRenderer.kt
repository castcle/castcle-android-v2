package com.castcle.android.presentation.top_trends.item_top_trends_title

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemTopTrendsTitleBinding
import com.castcle.android.presentation.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable

class TopTrendsTitleViewRenderer : CastcleViewRenderer<TopTrendsTitleViewEntity,
    TopTrendsTitleViewHolder,
    TopTrendsListener>(R.layout.item_top_trends_title) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: TopTrendsListener,
        compositeDisposable: CompositeDisposable
    ) = TopTrendsTitleViewHolder(
        ItemTopTrendsTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}