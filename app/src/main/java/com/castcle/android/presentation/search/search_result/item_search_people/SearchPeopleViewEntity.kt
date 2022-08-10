package com.castcle.android.presentation.search.search_result.item_search_people

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class SearchPeopleViewEntity(
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SearchPeopleViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SearchPeopleViewEntity>() == this
    }

    override fun viewType() = R.layout.item_search_people

}