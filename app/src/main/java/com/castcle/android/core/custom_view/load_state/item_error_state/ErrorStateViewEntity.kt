package com.castcle.android.core.custom_view.load_state.item_error_state

import com.castcle.android.R
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshViewEntity
import com.castcle.android.core.extensions.cast

data class ErrorStateViewEntity(
    override var action: () -> Unit = {},
    override var error: Throwable? = null,
    override val uniqueId: String = "${R.layout.item_error_state}",
) : LoadStateRefreshViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ErrorStateViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ErrorStateViewEntity>() == this
    }

    override fun viewType() = R.layout.item_error_state

    companion object {
        fun create(size: Int): List<ErrorStateViewEntity> {
            return mutableListOf<ErrorStateViewEntity>().apply {
                repeat(size) { add(ErrorStateViewEntity()) }
            }
        }
    }

}