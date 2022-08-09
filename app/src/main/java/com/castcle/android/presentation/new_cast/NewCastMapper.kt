package com.castcle.android.presentation.new_cast

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import org.koin.core.annotation.Factory

@Factory
class NewCastMapper {

    fun apply(item: CastWithUserEntity): CastcleViewEntity {
        return when {
            item.cast.image.isNotEmpty() || item.cast.linkPreview.isNotBlank() -> FeedImageViewEntity(
                cast = item.cast,
                imageItems = item.cast.image
                    .map { it }
                    .plus(ImageEntity.map(item.cast.linkPreview))
                    .filterNotNull(),
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            item.cast.linkUrl.isNotBlank() -> FeedWebViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            else -> FeedTextViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
        }
    }

}