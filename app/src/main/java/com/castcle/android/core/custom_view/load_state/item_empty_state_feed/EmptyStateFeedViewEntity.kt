package com.castcle.android.core.custom_view.load_state.item_empty_state_feed

import com.castcle.android.R
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshViewEntity
import com.castcle.android.core.extensions.cast

data class EmptyStateFeedViewEntity(
    override var action: (() -> Unit)? = null,
    override var error: Throwable? = null,
    override var refreshAction: () -> Unit = {},
    override var retryAction: () -> Unit = {},
    override val uniqueId: String = "${R.layout.item_empty_state_feed}",
) : LoadStateRefreshViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<EmptyStateFeedViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<EmptyStateFeedViewEntity>() == this
    }

    override fun viewType() = R.layout.item_empty_state_feed

    companion object {
        fun create(size: Int): List<EmptyStateFeedViewEntity> {
            return mutableListOf<EmptyStateFeedViewEntity>().apply {
                repeat(size) { add(EmptyStateFeedViewEntity()) }
            }
        }
    }

}