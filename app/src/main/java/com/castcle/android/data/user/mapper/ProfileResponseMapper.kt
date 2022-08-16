package com.castcle.android.data.user.mapper

import androidx.paging.LoadType
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.ProfileType
import org.koin.core.annotation.Factory

@Factory
class ProfileResponseMapper {

    data class ProfileResponseResult(
        val cast: List<CastEntity>,
        val profile: List<ProfileEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        currentUser: UserEntity,
        sessionId: Long,
        loadType: LoadType,
        ownerUserId: List<String>,
        profileResponse: BaseResponse<List<CastResponse>>?
    ): ProfileResponseResult {
        val profileItem = if (loadType == LoadType.REFRESH) {
            val item = ProfileEntity(
                createdAt = Long.MAX_VALUE,
                originalUserId = currentUser.id,
                sessionId = sessionId,
                type = ProfileType.Profile,
            )
            listOf(item)
        } else {
            listOf()
        }
        val newCastItems = if (loadType == LoadType.REFRESH && currentUser.isOwner) {
            val item = ProfileEntity(
                createdAt = Long.MAX_VALUE.minus(1),
                originalUserId = currentUser.id,
                sessionId = sessionId,
                type = ProfileType.NewCast,
            )
            listOf(item)
        } else {
            listOf()
        }
        val userItems = profileResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = profileResponse?.includes
            ?.casts
            ?.map { CastEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val feedItems = profileResponse?.payload.orEmpty().map { response ->
            val cast = CastEntity.map(ownerUserId, response).also(castItems::add)
            val referencedCast = castItems.find { it.id == response.referencedCasts?.id }
            ProfileEntity(
                createdAt = cast.createdAt.toMilliSecond() ?: 0L,
                originalCastId = cast.id,
                originalUserId = cast.authorId,
                referenceCastId = referencedCast?.id,
                referenceUserId = referencedCast?.authorId,
                sessionId = sessionId,
                type = ProfileType.Content,
            )
        }
        return ProfileResponseResult(
            cast = castItems.distinctBy { it.id },
            profile = profileItem.plus(newCastItems).plus(feedItems),
            user = userItems.distinctBy { it.id },
        )
    }

}