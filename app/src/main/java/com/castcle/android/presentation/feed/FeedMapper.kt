package com.castcle.android.presentation.feed

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.feed.entity.FeedWithResultEntity
import com.castcle.android.domain.feed.type.FeedType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image_1.FeedImage1ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_2.FeedImage2ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_3.FeedImage3ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_4.FeedImage4ViewEntity
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_who_to_follow.FeedWhoToFollowViewEntity
import org.koin.core.annotation.Factory

@Factory
class FeedMapper {

    fun apply(item: FeedWithResultEntity): CastcleViewEntity {
        return when (item.feed.type) {
            FeedType.NewCast -> FeedNewCastViewEntity(
                user = item.originalUser ?: UserEntity(),
            )
            FeedType.WhoToFollow -> FeedWhoToFollowViewEntity(
                feedId = item.feed.id,
                user1 = item.originalUser,
                user2 = item.referenceUser ?: item.originalUser,
            )
            else -> mapContentItem(item)
        }
    }

    private fun mapContentItem(item: FeedWithResultEntity): CastcleViewEntity {
        return when (item.originalCast?.type) {
            CastType.Quote -> FeedQuoteViewEntity(
                cast = item.originalCast,
                feedId = item.feed.id,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                uniqueId = item.feed.id,
                user = item.originalUser ?: UserEntity(),
            )
            CastType.Recast -> FeedRecastViewEntity(
                cast = item.originalCast,
                feedId = item.feed.id,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                uniqueId = item.feed.id,
                user = item.originalUser ?: UserEntity(),
            )
            else -> when {
                item.originalCast?.image?.size == 1 || item.originalCast?.linkPreview?.isNotBlank() == true -> FeedImage1ViewEntity(
                    cast = item.originalCast,
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.image?.size == 2 -> FeedImage2ViewEntity(
                    cast = item.originalCast,
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.image?.size == 3 -> FeedImage3ViewEntity(
                    cast = item.originalCast,
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                (item.originalCast?.image?.size ?: 0) >= 4 -> FeedImage4ViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl?.isNotBlank() == true -> FeedWebViewEntity(
                    cast = item.originalCast,
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                else -> FeedTextViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
            }
        }
    }

}