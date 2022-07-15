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
import org.koin.core.annotation.Factory

@Factory
class FeedResponseMapper {

    data class FeedResponseResult(
        val cast: List<CastEntity>,
        val feed: List<FeedEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        user: UserEntity?,
        loadType: LoadType,
        feedResponse: BaseResponse<FeedResponse>?,
        isGuest: Boolean,
    ): FeedResponseResult {
        val newCastItems = if (loadType == LoadType.REFRESH && !isGuest) {
            val item = FeedEntity(
                id = FeedType.NewCast.id,
                originalUserId = user?.id,
                type = FeedType.NewCast,
            )
            listOf(item)
        } else {
            listOf()
        }
        val userItems = feedResponse?.includes
            ?.users
            ?.map { UserEntity.map(user?.id, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = feedResponse?.includes
            ?.casts
            ?.map { CastEntity.map(user?.id, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val feedItems = feedResponse?.payload.orEmpty().mapNotNull { response ->
            when (val type = FeedType.getFromId(response.type)) {
                FeedType.AdsContent,
                FeedType.Content -> {
                    val payload = mapObject<CastResponse>(response.payload)
                    val cast = CastEntity.map(user?.id, payload).also(castItems::add)
                    val referencedCast = castItems.find { it.id == payload?.referencedCasts?.id }
                    FeedEntity(
                        campaignMessage = response.campaignMessage,
                        campaignName = response.campaignName,
                        id = response.id ?: "",
                        originalCastId = cast.id,
                        originalUserId = cast.authorId,
                        referenceCastId = referencedCast?.id,
                        referenceUserId = referencedCast?.authorId,
                        type = type,
                    )
                }
                FeedType.AdsPage -> {
                    val adsPage = mapObject<UserResponse>(response.payload)
                        .let { UserEntity.map(user?.id, it) }
                        .also(userItems::add)
                    FeedEntity(
                        campaignMessage = response.campaignMessage,
                        campaignName = response.campaignName,
                        id = response.id ?: "",
                        originalUserId = adsPage.id,
                        type = type,
                    )
                }
                FeedType.WhoToFollow -> {
                    val suggestionFollow = mapListObject<UserResponse>(response.payload)
                        .map { UserEntity.map(user?.id, it) }
                        .filter { !it.isOwner }
                        .also(userItems::addAll)
                    if (suggestionFollow.isEmpty()) {
                        null
                    } else {
                        FeedEntity(
                            id = response.id ?: "",
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