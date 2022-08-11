package com.castcle.android.presentation.feed

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.feed.entity.FeedWithResultEntity
import com.castcle.android.domain.feed.type.FeedType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_reporting.FeedReportingViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewEntity
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
        if (item.originalCast?.reporting == true || item.referenceCast?.reporting == true) {
            return FeedReportingViewEntity(
                reportingContentId = item.originalCast?.id,
                reportingReferenceContentId = item.referenceCast?.id,
            )
        }
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
                referenceCast = item.referenceCast,
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
                item.originalCast?.image.orEmpty().isNotEmpty() -> FeedImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkPreview.orEmpty().isNotBlank() -> FeedWebImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.id,
                    uniqueId = item.feed.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl.orEmpty().isNotBlank() -> FeedWebViewEntity(
                    cast = item.originalCast ?: CastEntity(),
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