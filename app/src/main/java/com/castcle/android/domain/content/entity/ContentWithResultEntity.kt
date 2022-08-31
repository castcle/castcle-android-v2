package com.castcle.android.domain.content.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity

data class ContentWithResultEntity(
    @Relation(
        parentColumn = "${TABLE_CONTENT}_commentId",
        entityColumn = "${TABLE_COMMENT}_id",
    )
    val comment: CommentEntity? = null,
    @Relation(
        parentColumn = "${TABLE_CONTENT}_commentUserId",
        entityColumn = "${TABLE_USER}_id",
    )
    val commentUser: UserEntity? = null,
    @Embedded val content: ContentEntity = ContentEntity(),
    @Relation(
        parentColumn = "${TABLE_CONTENT}_originalCastId",
        entityColumn = "${TABLE_CAST}_id",
    )
    val originalCast: CastEntity? = null,
    @Relation(
        parentColumn = "${TABLE_CONTENT}_originalUserId",
        entityColumn = "${TABLE_USER}_id",
    )
    val originalUser: UserEntity? = null,
    @Relation(
        parentColumn = "${TABLE_CONTENT}_referenceCastId",
        entityColumn = "${TABLE_CAST}_id",
    )
    val referenceCast: CastEntity? = null,
    @Relation(
        parentColumn = "${TABLE_CONTENT}_referenceUserId",
        entityColumn = "${TABLE_USER}_id",
    )
    val referenceUser: UserEntity? = null,
)