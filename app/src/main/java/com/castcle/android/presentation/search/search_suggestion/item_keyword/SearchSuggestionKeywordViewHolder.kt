package com.castcle.android.presentation.search.search_suggestion.item_keyword

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSearchSuggestionKeywordBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SearchSuggestionKeywordViewHolder(
    private val binding: ItemSearchSuggestionKeywordBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SearchSuggestionListener,
) : CastcleViewHolder<SearchSuggestionKeywordViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onKeywordClicked(item.slug)
        }
    }

    override var item = SearchSuggestionKeywordViewEntity()

    override fun bind(bindItem: SearchSuggestionKeywordViewEntity) {
        binding.tvKeyword.text = item.name
    }

}