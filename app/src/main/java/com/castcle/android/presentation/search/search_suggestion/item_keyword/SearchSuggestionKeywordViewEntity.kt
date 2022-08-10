package com.castcle.android.presentation.search.search_suggestion.item_keyword

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class SearchSuggestionKeywordViewEntity(
    val name: String = "",
    val slug: String = "",
    override val uniqueId: String = "",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SearchSuggestionKeywordViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SearchSuggestionKeywordViewEntity>() == this
    }


    override fun viewType() = R.layout.item_search_suggestion_keyword

}