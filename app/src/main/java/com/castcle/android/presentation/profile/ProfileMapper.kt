package com.castcle.android.presentation.profile

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.ProfileWithResultEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.ProfileType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_new_cast.FeedNewCastViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_reporting.FeedReportingViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.profile.item_profile_page.ProfilePageViewEntity
import com.castcle.android.presentation.profile.item_profile_user.ProfileUserViewEntity
import org.koin.core.annotation.Factory

@Factory
class ProfileMapper {

    fun apply(item: ProfileWithResultEntity): CastcleViewEntity {
        return when (item.profile.type) {
            ProfileType.NewCast -> FeedNewCastViewEntity(
                user = item.originalUser ?: UserEntity(),
            )
            ProfileType.Profile -> if (item.originalUser?.type is UserType.Page) {
                ProfilePageViewEntity(user = item.originalUser)
            } else {
                ProfileUserViewEntity(user = item.originalUser ?: UserEntity())
            }
            else -> mapContentItem(item)
        }
    }

    private fun mapContentItem(item: ProfileWithResultEntity): CastcleViewEntity {
        if (item.originalCast?.reporting == true || item.referenceCast?.reporting == true) {
            return FeedReportingViewEntity(
                reportingContentId = item.originalCast?.id,
                reportingReferenceContentId = item.referenceCast?.id,
            )
        }
        return when (item.originalCast?.type) {
            CastType.Quote -> FeedQuoteViewEntity(
                cast = item.originalCast,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                referenceCast = item.referenceCast,
                uniqueId = item.originalCast.id,
                user = item.originalUser ?: UserEntity(),
            )
            CastType.Recast -> FeedRecastViewEntity(
                cast = item.originalCast,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                uniqueId = item.originalCast.id,
                user = item.originalUser ?: UserEntity(),
            )
            else -> when {
                !item.originalCast?.image.isNullOrEmpty() || item.originalCast?.linkPreview?.isNotBlank() == true -> FeedImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    imageItems = item.originalCast?.image.orEmpty()
                        .map { it }
                        .plus(ImageEntity.map(item.originalCast?.linkPreview))
                        .filterNotNull(),
                    uniqueId = item.originalCast?.id.orEmpty(),
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl?.isNotBlank() == true -> FeedWebViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                else -> FeedTextViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id.orEmpty(),
                    user = item.originalUser ?: UserEntity(),
                )
            }
        }
    }

}