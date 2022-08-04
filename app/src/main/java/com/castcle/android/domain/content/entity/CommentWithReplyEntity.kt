package com.castcle.android.domain.content.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*

data class CommentWithReplyEntity(
    @Embedded val comment: CommentEntity = CommentEntity(),
    @Relation(parentColumn = "${TABLE_COMMENT}_id", entityColumn = "${TABLE_COMMENT}_parentId")
    val reply: List<CommentEntity> = listOf(),
)