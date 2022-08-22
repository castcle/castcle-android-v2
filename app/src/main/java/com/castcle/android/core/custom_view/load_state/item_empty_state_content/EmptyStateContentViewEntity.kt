package com.castcle.android.core.custom_view.load_state.item_empty_state_content

import com.castcle.android.R
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshViewEntity
import com.castcle.android.core.extensions.cast

data class EmptyStateContentViewEntity(
    override var action: () -> Unit = {},
    override var error: Throwable? = null,
    override val uniqueId: String = "${R.layout.item_empty_state_content}",
) : LoadStateRefreshViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<EmptyStateContentViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<EmptyStateContentViewEntity>() == this
    }

    override fun viewType() = R.layout.item_empty_state_content

    companion object {
        fun create(size: Int): List<EmptyStateContentViewEntity> {
            return mutableListOf<EmptyStateContentViewEntity>().apply {
                repeat(size) { add(EmptyStateContentViewEntity()) }
            }
        }
    }

}