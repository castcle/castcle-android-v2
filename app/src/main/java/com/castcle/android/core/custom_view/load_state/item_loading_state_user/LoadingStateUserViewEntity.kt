package com.castcle.android.core.custom_view.load_state.item_loading_state_user

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingStateUserViewEntity(
    override val uniqueId: String = "${R.layout.item_loading_state_user}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingStateUserViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingStateUserViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading_state_user

    companion object {
        fun create(size: Int): List<LoadingStateUserViewEntity> {
            return mutableListOf<LoadingStateUserViewEntity>().apply {
                repeat(size) { add(LoadingStateUserViewEntity()) }
            }
        }
    }

}