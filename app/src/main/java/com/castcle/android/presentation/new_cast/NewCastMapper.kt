package com.castcle.android.presentation.new_cast

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewEntity
import org.koin.core.annotation.Factory

@Factory
class NewCastMapper {

    fun apply(item: CastWithUserEntity): CastcleViewEntity {
        return when {
            item.cast.image.isNotEmpty() -> FeedImageViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            item.cast.linkPreview.isNotBlank() -> FeedWebImageViewEntity(
                cast = item.cast,
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