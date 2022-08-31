package com.castcle.android.presentation.search.search_suggestion.item_user

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSearchSuggestionUserBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable

class SearchSuggestionUserViewRenderer : CastcleViewRenderer<SearchSuggestionUserViewEntity,
    SearchSuggestionUserViewHolder,
    SearchSuggestionListener>(R.layout.item_search_suggestion_user) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SearchSuggestionListener,
        compositeDisposable: CompositeDisposable
    ) = SearchSuggestionUserViewHolder(
        ItemSearchSuggestionUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}