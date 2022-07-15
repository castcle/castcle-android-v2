package com.castcle.android.core.custom_view.load_state.item_loading_state_profile

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingStateProfileViewEntity(
    override val uniqueId: String = "${R.layout.item_loading_state_profile}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingStateProfileViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingStateProfileViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading_state_profile

    companion object {
        fun create(size: Int): List<LoadingStateProfileViewEntity> {
            return mutableListOf<LoadingStateProfileViewEntity>().apply {
                repeat(size) { add(LoadingStateProfileViewEntity()) }
            }
        }
    }

}