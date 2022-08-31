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