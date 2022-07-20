package com.castcle.android.presentation.search_suggestion.item_title

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class SearchSuggestionTitleViewEntity(
    override val uniqueId: String = "${R.layout.item_search_suggestion_title}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SearchSuggestionTitleViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SearchSuggestionTitleViewEntity>() == this
    }


    override fun viewType() = R.layout.item_search_suggestion_title

}