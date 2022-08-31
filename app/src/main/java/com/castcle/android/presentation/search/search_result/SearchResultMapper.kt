package com.castcle.android.presentation.search.search_result

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.search.entity.SearchWithResultEntity
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_report.FeedReportViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewEntity
import com.castcle.android.presentation.search.search_result.item_search_people.SearchPeopleViewEntity
import org.koin.core.annotation.Factory

@Factory
class SearchResultMapper {

    fun apply(item: SearchWithResultEntity, type: SearchType): CastcleViewEntity {
        return if (type is SearchType.People) {
            SearchPeopleViewEntity(
                uniqueId = item.originalUser?.id.orEmpty(),
                user = item.originalUser ?: UserEntity(),
            )
        } else {
            mapContentItem(item)
        }
    }

    private fun mapContentItem(item: SearchWithResultEntity): CastcleViewEntity {
        when {
            item.originalCast?.reported == true &&
                !item.search.ignoreReportContentId.contains(item.originalCast.id) -> {
                return FeedReportViewEntity(
                    ignoreReportContentId = item.search.ignoreReportContentId.plus(item.originalCast.id),
                    uniqueId = item.search.id.toString(),
                )
            }
            item.referenceCast?.reported == true &&
                item.originalCast?.type is CastType.Recast &&
                !item.search.ignoreReportContentId.contains(item.referenceCast.id) -> {
                return FeedReportViewEntity(
                    ignoreReportContentId = item.search.ignoreReportContentId.plus(item.referenceCast.id),
                    uniqueId = item.search.id.toString(),
                )
            }
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
                item.originalCast?.image.orEmpty().isNotEmpty() -> FeedImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id.orEmpty(),
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkPreview.orEmpty().isNotBlank() -> FeedWebImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id.orEmpty(),
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl.orEmpty().isNotBlank() -> FeedWebViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    uniqueId = item.originalCast?.id.orEmpty(),
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