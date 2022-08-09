package com.castcle.android.presentation.content

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentWithResultEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.content.item_comment.CommentViewEntity
import com.castcle.android.presentation.content.item_content_metrics.ContentMetricsViewEntity
import com.castcle.android.presentation.content.item_reply.ReplyViewEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_quote.FeedQuoteViewEntity
import com.castcle.android.presentation.feed.item_feed_recast.FeedRecastViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import org.koin.core.annotation.Factory

@Factory
class ContentMapper {

    fun apply(item: ContentWithResultEntity): CastcleViewEntity {
        return when (item.content.type) {
            is ContentType.Comment -> mapCommentItem(item)
            is ContentType.Content -> mapContentItem(item)
            is ContentType.Metrics -> mapMetricsItem(item)
            is ContentType.Reply -> mapReplyItem(item)
        }
    }

    private fun mapReplyItem(item: ContentWithResultEntity): CastcleViewEntity {
        return ReplyViewEntity(
            comment = item.comment ?: CommentEntity(),
            showLine = item.content.isLastComment,
            uniqueId = item.comment?.id ?: "",
            user = item.commentUser ?: UserEntity(),
        )
    }

    private fun mapMetricsItem(item: ContentWithResultEntity): ContentMetricsViewEntity {
        val targetContent = if (item.originalCast?.type == CastType.Recast) {
            item.referenceCast
        } else {
            item.originalCast
        }
        return ContentMetricsViewEntity(
            contentId = targetContent?.id ?: "",
            likeCount = targetContent?.likeCount ?: 0,
            quoteCount = targetContent?.quoteCount ?: 0,
            recastCount = targetContent?.recastCount ?: 0,
        )
    }

    private fun mapCommentItem(item: ContentWithResultEntity): CastcleViewEntity {
        return CommentViewEntity(
            comment = item.comment ?: CommentEntity(),
            showLine = item.content.isLastComment,
            uniqueId = item.comment?.id ?: "",
            user = item.commentUser ?: UserEntity(),
        )
    }

    private fun mapContentItem(item: ContentWithResultEntity): CastcleViewEntity {
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