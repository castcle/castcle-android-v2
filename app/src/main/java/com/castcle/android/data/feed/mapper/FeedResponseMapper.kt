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

package com.castcle.android.data.feed.mapper

import androidx.paging.LoadType
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.extensions.*
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.feed.entity.FeedResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.feed.entity.FeedEntity
import com.castcle.android.domain.feed.type.FeedType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import org.koin.core.annotation.Factory

@Factory
class FeedResponseMapper {

    data class FeedResponseResult(
        val cast: List<CastEntity>,
        val feed: List<FeedEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        feedResponse: BaseResponse<List<FeedResponse>>?,
        isGuest: Boolean,
        loadType: LoadType,
        ownerUser: List<UserEntity>,
    ): FeedResponseResult {
        val ownerUserId = ownerUser.map { it.id }
        val userId = ownerUser.find { it.type is UserType.People }?.id.orEmpty()
        val newCastItems = if (loadType == LoadType.REFRESH && !isGuest) {
            val item = FeedEntity(
                originalUserId = userId,
                type = FeedType.NewCast,
            )
            listOf(item)
        } else {
            listOf()
        }
        val userItems = feedResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = feedResponse?.includes
            ?.casts
            ?.map { CastEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val feedItems = feedResponse?.payload.orEmpty().mapNotNull { response ->
            when (val type = FeedType.getFromId(response.type)) {
                FeedType.AdsContent,
                FeedType.Content -> {
                    val payload = mapObject<CastResponse>(response.payload)
                    val cast = CastEntity.map(ownerUserId, payload).also(castItems::add)
                    val referencedCast = castItems.find { it.id == payload?.referencedCasts?.id }
                    FeedEntity(
                        campaignMessage = response.campaignMessage,
                        campaignName = response.campaignName,
                        feedId = response.id.orEmpty(),
                        originalCastId = cast.id,
                        originalUserId = cast.authorId,
                        referenceCastId = referencedCast?.id,
                        referenceUserId = referencedCast?.authorId,
                        type = type,
                    )
                }
                FeedType.AdsPage -> {
                    val adsPage = mapObject<UserResponse>(response.payload)
                        .let { UserEntity.map(ownerUserId, it) }
                        .also(userItems::add)
                    FeedEntity(
                        campaignMessage = response.campaignMessage,
                        campaignName = response.campaignName,
                        feedId = response.id.orEmpty(),
                        originalUserId = adsPage.id,
                        type = type,
                    )
                }
                FeedType.WhoToFollow -> {
                    val suggestionFollow = mapListObject<UserResponse>(response.payload)
                        .map { UserEntity.map(ownerUserId, it) }
                        .filter { !it.isOwner }
                        .also(userItems::addAll)
                    if (suggestionFollow.isEmpty()) {
                        null
                    } else {
                        FeedEntity(
                            feedId = response.id.orEmpty(),
                            originalUserId = suggestionFollow.firstOrNull()?.id,
                            referenceUserId = suggestionFollow.secondOrNull()?.id,
                            type = type,
                        )
                    }
                }
                else -> null
            }
        }
        return FeedResponseResult(
            cast = castItems.distinctBy { it.id },
            feed = newCastItems.plus(feedItems),
            user = userItems.distinctBy { it.id },
        )
    }

}