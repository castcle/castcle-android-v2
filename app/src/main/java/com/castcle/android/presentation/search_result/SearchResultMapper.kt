package com.castcle.android.presentation.search_result

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.search.entity.SearchWithResultEntity
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image_1.FeedImage1ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_2.FeedImage2ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_3.FeedImage3ViewEntity
import com.castcle.android.presentation.feed.item_feed_image_4.FeedImage4ViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.search_result.item_search_people.SearchPeopleViewEntity
import com.castcle.android.presentation.who_to_follow.item_who_to_follow.WhoToFollowViewEntity
import org.koin.core.annotation.Factory

@Factory
class SearchResultMapper {

    fun apply(item: SearchWithResultEntity, type: SearchType): CastcleViewEntity {
        return if (type is SearchType.People) {
            SearchPeopleViewEntity(
                uniqueId = item.originalUser?.id ?: "",
                user = item.originalUser ?: UserEntity(),
            )
//            FeedTextViewEntity(
//                cast = item.originalCast ?: CastEntity(),
//                uniqueId = item.originalCast?.id ?: "",
//                user = item.originalUser ?: UserEntity(),
//            )
        } else {
            mapContentItem(item)
        }
    }

    private fun mapContentItem(item: SearchWithResultEntity): CastcleViewEntity {
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
            CastType.Image -> when (item.originalCast.image.size) {
                0 -> FeedTextViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                1 -> FeedImage1ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                2 -> FeedImage2ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                3 -> FeedImage3ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                else -> FeedImage4ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
            }
            else -> when {
                item.originalCast?.image?.size == 1 || item.originalCast?.linkPreview?.isNotBlank() == true -> FeedImage1ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.image?.size == 2 -> FeedImage1ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.image?.size == 3 -> FeedImage2ViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                (item.originalCast?.image?.size ?: 0) >= 4 -> FeedImage4ViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id ?: "",
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl?.isNotBlank() == true -> FeedWebViewEntity(
                    cast = item.originalCast,
                    uniqueId = item.originalCast.id,
                    user = item.originalUser ?: UserEntity(),
                )
                else -> FeedTextViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id ?: "",
                    user = item.originalUser ?: UserEntity(),
                )
            }
        }
    }

}