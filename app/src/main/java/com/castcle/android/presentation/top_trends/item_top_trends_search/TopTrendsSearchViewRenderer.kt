package com.castcle.android.presentation.top_trends.item_top_trends_search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemTopTrendsSearchBinding
import com.castcle.android.presentation.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable

class TopTrendsSearchViewRenderer : CastcleViewRenderer<TopTrendsSearchViewEntity,
    TopTrendsSearchViewHolder,
    TopTrendsListener>(R.layout.item_top_trends_search) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: TopTrendsListener,
        compositeDisposable: CompositeDisposable
    ) = TopTrendsSearchViewHolder(
        ItemTopTrendsSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}