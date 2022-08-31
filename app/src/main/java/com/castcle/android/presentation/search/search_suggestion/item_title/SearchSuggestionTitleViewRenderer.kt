package com.castcle.android.presentation.search.search_suggestion.item_title

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSearchSuggestionTitleBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable

class SearchSuggestionTitleViewRenderer : CastcleViewRenderer<SearchSuggestionTitleViewEntity,
    SearchSuggestionTitleViewHolder,
    SearchSuggestionListener>(R.layout.item_search_suggestion_title) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SearchSuggestionListener,
        compositeDisposable: CompositeDisposable
    ) = SearchSuggestionTitleViewHolder(
        ItemSearchSuggestionTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}