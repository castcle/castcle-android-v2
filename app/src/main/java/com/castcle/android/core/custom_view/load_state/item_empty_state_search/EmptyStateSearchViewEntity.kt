package com.castcle.android.core.custom_view.load_state.item_empty_state_search

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class EmptyStateSearchViewEntity(
    override val uniqueId: String = "${R.layout.item_empty_state_search}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<EmptyStateSearchViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<EmptyStateSearchViewEntity>() == this
    }

    override fun viewType() = R.layout.item_empty_state_search

    companion object {
        fun create(size: Int): List<EmptyStateSearchViewEntity> {
            return mutableListOf<EmptyStateSearchViewEntity>().apply {
                repeat(size) { add(EmptyStateSearchViewEntity()) }
            }
        }
    }

}