package com.castcle.android.core.custom_view.load_state.item_loading_state_cast

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingStateCastViewEntity(
    override val uniqueId: String = "${R.layout.item_loading_state_cast}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingStateCastViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingStateCastViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading_state_cast

    companion object {
        fun create(size: Int): List<LoadingStateCastViewEntity> {
            return mutableListOf<LoadingStateCastViewEntity>().apply {
                repeat(size) { add(LoadingStateCastViewEntity()) }
            }
        }
    }

}