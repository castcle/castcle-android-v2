package com.castcle.android.domain.user.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.cast.entity.CastEntity

data class ProfileWithResultEntity(
    @Relation(parentColumn = "${TABLE_PROFILE}_originalCastId", entityColumn = "${TABLE_CAST}_id")
    val originalCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_PROFILE}_originalUserId", entityColumn = "${TABLE_USER}_id")
    val originalUser: UserEntity? = null,
    @Embedded val profile: ProfileEntity = ProfileEntity(),
    @Relation(parentColumn = "${TABLE_PROFILE}_referenceCastId", entityColumn = "${TABLE_CAST}_id")
    val referenceCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_PROFILE}_referenceUserId", entityColumn = "${TABLE_USER}_id")
    val referenceUser: UserEntity? = null,
)