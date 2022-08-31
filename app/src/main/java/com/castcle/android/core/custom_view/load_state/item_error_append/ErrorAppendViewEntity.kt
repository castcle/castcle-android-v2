package com.castcle.android.core.custom_view.load_state.item_error_append

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ErrorAppendViewEntity(
    val action: () -> Unit = {},
    val errorMessage: String? = null,
    override val uniqueId: String = "${R.layout.item_error_append}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ErrorAppendViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ErrorAppendViewEntity>() == this
    }

    override fun viewType() = R.layout.item_error_append

    companion object {
        fun create(size: Int): List<ErrorAppendViewEntity> {
            return mutableListOf<ErrorAppendViewEntity>().apply {
                repeat(size) { add(ErrorAppendViewEntity()) }
            }
        }
    }

}