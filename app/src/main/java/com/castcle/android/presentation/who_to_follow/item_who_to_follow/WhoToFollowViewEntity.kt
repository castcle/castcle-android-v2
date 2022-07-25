package com.castcle.android.presentation.who_to_follow.item_who_to_follow

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.entity.WhoToFollowWithResultEntity

data class WhoToFollowViewEntity(
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WhoToFollowViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WhoToFollowViewEntity>() == this
    }

    override fun viewType() = R.layout.item_who_to_follow

    companion object {
        fun map(response: WhoToFollowWithResultEntity): CastcleViewEntity = WhoToFollowViewEntity(
            uniqueId = response.whoToFollow.id.toString(),
            user = response.user ?: UserEntity(),
        )
    }

}