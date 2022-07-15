package com.castcle.android.presentation.feed.item_feed_new_cast

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.user.entity.UserEntity

data class FeedNewCastViewEntity(
    override val uniqueId: String = "${R.layout.item_feed_new_cast}",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedNewCastViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedNewCastViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_new_cast

}