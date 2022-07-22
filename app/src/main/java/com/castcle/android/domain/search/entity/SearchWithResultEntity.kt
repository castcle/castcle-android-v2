package com.castcle.android.domain.search.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity

data class SearchWithResultEntity(
    @Embedded val search: SearchEntity = SearchEntity(),
    @Relation(parentColumn = "${TABLE_SEARCH}_originalCastId", entityColumn = "${TABLE_CAST}_id")
    val originalCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_originalUserId", entityColumn = "${TABLE_USER}_id")
    val originalUser: UserEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_referenceCastId", entityColumn = "${TABLE_CAST}_id")
    val referenceCast: CastEntity? = null,
    @Relation(parentColumn = "${TABLE_SEARCH}_referenceUserId", entityColumn = "${TABLE_USER}_id")
    val referenceUser: UserEntity? = null,
)