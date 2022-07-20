package com.castcle.android.presentation.search_suggestion.item_user

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class SearchSuggestionUserViewEntity(
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SearchSuggestionUserViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SearchSuggestionUserViewEntity>() == this
    }


    override fun viewType() = R.layout.item_search_suggestion_user

}