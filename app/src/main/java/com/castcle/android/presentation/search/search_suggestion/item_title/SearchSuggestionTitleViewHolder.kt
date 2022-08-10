package com.castcle.android.presentation.search.search_suggestion.item_title

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSearchSuggestionTitleBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SearchSuggestionTitleViewHolder(
    binding: ItemSearchSuggestionTitleBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SearchSuggestionListener,
) : CastcleViewHolder<SearchSuggestionTitleViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.ivClear.onClick {
            listener.onClearRecentSearch()
        }
    }

    override var item = SearchSuggestionTitleViewEntity()

}