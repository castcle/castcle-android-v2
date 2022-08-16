package com.castcle.android.presentation.dialog.recast.item_select_recast_user

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class SelectRecastUserViewEntity(
    val isSelected: Boolean = false,
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<SelectRecastUserViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<SelectRecastUserViewEntity>() == this
    }

    override fun viewType() = R.layout.item_select_recast_user

}