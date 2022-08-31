package com.castcle.android.presentation.search.search_suggestion.item_keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSearchSuggestionKeywordBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable

class SearchSuggestionKeywordViewRenderer : CastcleViewRenderer<SearchSuggestionKeywordViewEntity,
    SearchSuggestionKeywordViewHolder,
    SearchSuggestionListener>(R.layout.item_search_suggestion_keyword) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SearchSuggestionListener,
        compositeDisposable: CompositeDisposable
    ) = SearchSuggestionKeywordViewHolder(
        ItemSearchSuggestionKeywordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}