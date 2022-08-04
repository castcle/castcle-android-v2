package com.castcle.android.domain.content.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_COMMENT
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.content.entity.CommentResponse

@Entity(tableName = TABLE_COMMENT)
data class CommentEntity(
    @ColumnInfo(name = "${TABLE_COMMENT}_authorId") val authorId: String = "",
    @ColumnInfo(name = "${TABLE_COMMENT}_createdAt") val createdAt: Long = 0L,
    @ColumnInfo(name = "${TABLE_COMMENT}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_COMMENT}_isOwner") val isOwner: Boolean = false,
    @ColumnInfo(name = "${TABLE_COMMENT}_likeCount") val likeCount: Int = 0,
    @ColumnInfo(name = "${TABLE_COMMENT}_liked") val liked: Boolean = false,
    @ColumnInfo(name = "${TABLE_COMMENT}_message") val message: String = "",
) {

    companion object {
        fun map(ownerUserId: List<String?>?, response: List<CommentResponse>?) =
            response.orEmpty().map { map(ownerUserId, it) }.toMutableList()

        fun map(ownerUserId: List<String?>?, response: CommentResponse?) = CommentEntity(
            authorId = response?.authorId ?: response?.author ?: "",
            createdAt = response?.createdAt.toMilliSecond(),
            id = response?.id ?: "",
            isOwner = ownerUserId.orEmpty()
                .filterNotNullOrBlank()
                .contains(response?.authorId ?: response?.author),
            liked = response?.participate?.liked ?: false,
            likeCount = response?.metrics?.likeCount ?: 0,
            message = response?.message ?: "",
        )
    }

}