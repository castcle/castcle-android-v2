package com.castcle.android.core.custom_view.load_state.item_loading

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingViewEntity(
    override val uniqueId: String = "${R.layout.item_loading}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading

}