/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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
import com.castcle.android.presentation.feed.item_feed_report.FeedReportViewEntity
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
                feedId = item.feed.feedId,
                user1 = item.originalUser,
                user2 = item.referenceUser ?: item.originalUser,
            )
            else -> mapContentItem(item)
        }
    }

    private fun mapContentItem(item: FeedWithResultEntity): CastcleViewEntity {
        when {
            item.originalCast?.reported == true &&
                !item.feed.ignoreReportContentId.contains(item.originalCast.id) -> {
                return FeedReportViewEntity(
                    ignoreReportContentId = item.feed.ignoreReportContentId.plus(item.originalCast.id),
                    uniqueId = item.feed.id.toString(),
                )
            }
            item.referenceCast?.reported == true &&
                item.originalCast?.type is CastType.Recast &&
                !item.feed.ignoreReportContentId.contains(item.referenceCast.id) -> {
                return FeedReportViewEntity(
                    ignoreReportContentId = item.feed.ignoreReportContentId.plus(item.referenceCast.id),
                    uniqueId = item.feed.id.toString(),
                )
            }
        }
        return when (item.originalCast?.type) {
            CastType.Quote -> FeedQuoteViewEntity(
                cast = item.originalCast,
                feedId = item.feed.feedId,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                referenceCast = item.referenceCast,
                uniqueId = item.feed.id.toString(),
                user = item.originalUser ?: UserEntity(),
                quoteIsOwner = item.referenceCast?.isOwner ?: false
            )
            CastType.Recast -> FeedRecastViewEntity(
                cast = item.originalCast,
                feedId = item.feed.feedId,
                reference = mapContentItem(
                    item.copy(
                        originalCast = item.referenceCast,
                        originalUser = item.referenceUser,
                        referenceCast = null,
                        referenceUser = null,
                    )
                ),
                uniqueId = item.feed.id.toString(),
                user = item.originalUser ?: UserEntity(),
            )
            else -> when {
                item.originalCast?.image.orEmpty().isNotEmpty() -> FeedImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.feedId,
                    uniqueId = item.feed.id.toString(),
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkPreview.orEmpty().isNotBlank() -> FeedWebImageViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.feedId,
                    uniqueId = item.feed.id.toString(),
                    user = item.originalUser ?: UserEntity(),
                )
                item.originalCast?.linkUrl.orEmpty().isNotBlank() -> FeedWebViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.feedId,
                    uniqueId = item.feed.id.toString(),
                    user = item.originalUser ?: UserEntity(),
                )
                else -> FeedTextViewEntity(
                    cast = item.originalCast ?: CastEntity(),
                    feedId = item.feed.feedId,
                    uniqueId = item.feed.id.toString(),
                    user = item.originalUser ?: UserEntity(),
                )
            }
        }
    }

}