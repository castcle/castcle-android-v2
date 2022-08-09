package com.castcle.android.presentation.dialog.recast.item_recast_title

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class RecastTitleViewEntity(
    override val uniqueId: String = "${R.layout.item_recast_title}",
    val user: UserEntity? = null,
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<RecastTitleViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<RecastTitleViewEntity>() == this
    }

    override fun viewType() = R.layout.item_recast_title

}