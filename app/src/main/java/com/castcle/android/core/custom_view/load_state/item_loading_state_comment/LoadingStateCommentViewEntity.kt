package com.castcle.android.core.custom_view.load_state.item_loading_state_comment

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class LoadingStateCommentViewEntity(
    override val uniqueId: String = "${R.layout.item_loading_state_comment}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<LoadingStateCommentViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<LoadingStateCommentViewEntity>() == this
    }

    override fun viewType() = R.layout.item_loading_state_comment

    companion object {
        fun create(size: Int): List<LoadingStateCommentViewEntity> {
            return mutableListOf<LoadingStateCommentViewEntity>().apply {
                repeat(size) { add(LoadingStateCommentViewEntity()) }
            }
        }
    }

}