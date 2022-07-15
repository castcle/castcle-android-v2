package com.castcle.android.presentation.feed.item_feed_image_4

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity

data class FeedImage4ViewEntity(
    val cast: CastEntity = CastEntity(),
    val feedId: String = "",
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedImage4ViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedImage4ViewEntity>() == this
    }


    override fun viewType() = R.layout.item_feed_image_4

}