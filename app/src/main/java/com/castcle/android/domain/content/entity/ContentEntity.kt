package com.castcle.android.domain.content.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CONTENT
import com.castcle.android.domain.content.type.ContentType

@Entity(tableName = TABLE_CONTENT)
data class ContentEntity(
    @ColumnInfo(name = "${TABLE_CONTENT}_commentId", defaultValue = "NULL")
    val commentId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_commentUserId", defaultValue = "NULL")
    val commentUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_createdAt", defaultValue = "0")
    val createdAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_id", defaultValue = "0") @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_ignoreReportContentId", defaultValue = "[]")
    val ignoreReportContentId: List<String> = listOf(),
    @ColumnInfo(name = "${TABLE_CONTENT}_isLastComment", defaultValue = "0")
    val isLastComment: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalCastId", defaultValue = "NULL")
    val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalUserId", defaultValue = "NULL")
    val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceCastId", defaultValue = "NULL")
    val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceUserId", defaultValue = "NULL")
    val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_replyAt", defaultValue = "0")
    val replyAt: Long = Long.MIN_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_sessionId", defaultValue = "0")
    val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_type", defaultValue = "")
    val type: ContentType = ContentType.Content,
) {

    fun toMetricsItem() = copy(
        createdAt = createdAt.minus(1),
        type = ContentType.Metrics,
    )

}