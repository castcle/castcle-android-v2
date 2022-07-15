package com.castcle.android.presentation.profile.item_profile_page

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class ProfilePageViewEntity(
    override val uniqueId: String = "${R.layout.item_profile_page}",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ProfilePageViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ProfilePageViewEntity>() == this
    }

    override fun viewType() = R.layout.item_profile_page

}