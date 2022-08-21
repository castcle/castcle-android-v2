package com.castcle.android.core.custom_view.load_state.item_empty_state_profile

import com.castcle.android.R
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshViewEntity
import com.castcle.android.core.extensions.cast

data class EmptyStateProfileViewEntity(
    override var action: () -> Unit = {},
    override var error: Throwable? = null,
    override val uniqueId: String = "${R.layout.item_empty_state_profile}",
) : LoadStateRefreshViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<EmptyStateProfileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<EmptyStateProfileViewEntity>() == this
    }

    override fun viewType() = R.layout.item_empty_state_profile

    companion object {
        fun create(size: Int): List<EmptyStateProfileViewEntity> {
            return mutableListOf<EmptyStateProfileViewEntity>().apply {
                repeat(size) { add(EmptyStateProfileViewEntity()) }
            }
        }
    }

}