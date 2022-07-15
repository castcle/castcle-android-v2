package com.castcle.android.domain.user.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.core.constants.TABLE_WHO_TO_FOLLOW

data class WhoToFollowWithResultEntity(
    @Relation(parentColumn = "${TABLE_WHO_TO_FOLLOW}_userId", entityColumn = "${TABLE_USER}_id")
    val user: UserEntity? = null,
    @Embedded val whoToFollow: WhoToFollowEntity = WhoToFollowEntity(),
)