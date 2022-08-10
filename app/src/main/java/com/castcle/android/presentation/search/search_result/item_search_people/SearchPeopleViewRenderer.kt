package com.castcle.android.presentation.search.search_result.item_search_people

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSearchPeopleBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class SearchPeopleViewRenderer : CastcleViewRenderer<SearchPeopleViewEntity,
    SearchPeopleViewHolder,
    FeedListener>(R.layout.item_search_people) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = SearchPeopleViewHolder(
        ItemSearchPeopleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}