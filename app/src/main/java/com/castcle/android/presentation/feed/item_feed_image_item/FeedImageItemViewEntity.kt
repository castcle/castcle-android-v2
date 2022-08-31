package com.castcle.android.presentation.feed.item_feed_image_item

import android.net.Uri
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.core.entity.ImageEntity

data class FeedImageItemViewEntity(
    val image: ImageEntity = ImageEntity(),
    val itemCount: Int = 0,
    override val uniqueId: String = "",
    val uri: Uri? = null,
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedImageItemViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedImageItemViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_image_item

}