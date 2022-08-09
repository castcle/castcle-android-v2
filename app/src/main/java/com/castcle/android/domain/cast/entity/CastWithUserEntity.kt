package com.castcle.android.domain.cast.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.domain.user.entity.UserEntity

data class CastWithUserEntity(
    @Embedded
    val cast: CastEntity = CastEntity(),
    @Relation(parentColumn = "${TABLE_CAST}_authorId", entityColumn = "${TABLE_USER}_id")
    val user: UserEntity? = null,
)