package com.castcle.android.domain.content.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CONTENT
import com.castcle.android.domain.content.type.ContentType

@Entity(tableName = TABLE_CONTENT)
data class ContentEntity(
    @ColumnInfo(name = "${TABLE_CONTENT}_commentId") val commentId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_commentUserId") val commentUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_createdAt") val createdAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_id") @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_isLastComment") val isLastComment: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalCastId") val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalUserId") val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceCastId") val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceUserId") val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_replyAt") val replyAt: Long = Long.MIN_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_sessionId") val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_type") val type: ContentType = ContentType.Content,
) {

    fun toMetricsItem() = copy(
        createdAt = createdAt.minus(1),
        type = ContentType.Metrics,
    )

}