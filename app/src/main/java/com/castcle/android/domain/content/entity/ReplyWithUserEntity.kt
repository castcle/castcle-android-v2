package com.castcle.android.domain.content.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_COMMENT
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.domain.user.entity.UserEntity

data class ReplyWithUserEntity(
    @Embedded val comment: CommentEntity = CommentEntity(),
    @Relation(parentColumn = "${TABLE_COMMENT}_authorId", entityColumn = "${TABLE_USER}_id")
    val user: UserEntity? = null,
)