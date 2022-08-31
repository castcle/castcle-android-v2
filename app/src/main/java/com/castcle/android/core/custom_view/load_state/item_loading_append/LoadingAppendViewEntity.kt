package com.castcle.android.core.custom_view.load_state.item_loading_append

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingAppendViewEntity(
    override val uniqueId: String = "${R.layout.item_loading_append}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingAppendViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingAppendViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading_append

    companion object {
        fun create(size: Int): List<LoadingAppendViewEntity> {
            return mutableListOf<LoadingAppendViewEntity>().apply {
                repeat(size) { add(LoadingAppendViewEntity()) }
            }
        }
    }

}