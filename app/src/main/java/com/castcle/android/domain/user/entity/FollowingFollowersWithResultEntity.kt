package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.*

@Entity(tableName = TABLE_FOLLOWING_FOLLOWERS)
data class FollowingFollowersWithResultEntity(
    @Embedded val followingFollowers: FollowingFollowersEntity = FollowingFollowersEntity(),
    @Relation(parentColumn = "${TABLE_FOLLOWING_FOLLOWERS}_userId", entityColumn = "${TABLE_USER}_id")
    val user: UserEntity? = null,
)