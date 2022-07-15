package com.castcle.android.presentation.feed.item_feed_who_to_follow

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class FeedWhoToFollowViewEntity(
    val feedId: String = "",
    override val uniqueId: String = feedId,
    val user1: UserEntity? = UserEntity(),
    val user2: UserEntity? = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedWhoToFollowViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedWhoToFollowViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_who_to_follow

}