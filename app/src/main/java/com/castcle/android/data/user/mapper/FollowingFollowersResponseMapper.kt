package com.castcle.android.data.user.mapper

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.user.entity.FollowingFollowersEntity
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class FollowingFollowersResponseMapper {

    data class FollowingFollowersResponseResult(
        val followingFollowers: List<FollowingFollowersEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        ownerUserId: List<String>,
        response: BaseResponse<UserResponse>?,
        sessionId: Long,
    ): FollowingFollowersResponseResult {
        val userItems = mutableListOf<UserEntity>()
        val followingFollowersItems = response?.payload.orEmpty().map {
            FollowingFollowersEntity(
                sessionId = sessionId,
                userId = UserEntity.map(ownerUserId, it).also(userItems::add).id,
            )
        }
        return FollowingFollowersResponseResult(
            followingFollowers = followingFollowersItems,
            user = userItems.distinctBy { it.id },
        )
    }

}