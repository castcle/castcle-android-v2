package com.castcle.android.presentation.search.top_trends.item_top_trends_search

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemTopTrendsSearchBinding
import com.castcle.android.presentation.search.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class TopTrendsSearchViewHolder(
    binding: ItemTopTrendsSearchBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: TopTrendsListener,
) : CastcleViewHolder<TopTrendsSearchViewEntity>(binding.root) {

    init {
        binding.searchBar.enabled(false)
        compositeDisposable += binding.viewSearch.onClick {
            listener.onSearchClicked()
        }
    }

    override var item = TopTrendsSearchViewEntity()

}