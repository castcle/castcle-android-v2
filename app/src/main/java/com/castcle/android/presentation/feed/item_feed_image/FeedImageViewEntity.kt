package com.castcle.android.presentation.feed.item_feed_image

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity

data class FeedImageViewEntity(
    val cast: CastEntity = CastEntity(),
    val feedId: String = "",
    val imageItems: List<ImageEntity> = listOf(),
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedImageViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedImageViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_image

}