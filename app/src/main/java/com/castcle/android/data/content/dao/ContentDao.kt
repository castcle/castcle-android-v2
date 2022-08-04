package com.castcle.android.data.content.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_CONTENT
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.entity.ContentWithResultEntity
import com.castcle.android.domain.content.type.ContentType

@Dao
interface ContentDao {

    @Query("DELETE FROM $TABLE_CONTENT")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_CONTENT WHERE content_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Query("SELECT DISTINCT content_sessionId FROM $TABLE_CONTENT WHERE content_originalCastId = :castId AND content_type = :type")
    suspend fun getExistSessionIdByCastId(
        castId: String,
        type: ContentType,
    ): List<Long>

    @Query("SELECT * FROM $TABLE_CONTENT WHERE content_commentId = :commentId AND content_type = :type")
    suspend fun getExistSessionIdByCommentId(
        commentId: String,
        type: ContentType,
    ): List<ContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ContentEntity>)

    @Query("SELECT * FROM $TABLE_CONTENT WHERE content_sessionId = :sessionId ORDER BY content_createdAt DESC, content_replyAt ASC")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, ContentWithResultEntity>

    @Query(
        "UPDATE $TABLE_CONTENT SET content_isLastComment = CASE WHEN content_createdAt IS " +
            "(SELECT MIN(content_createdAt) FROM $TABLE_CONTENT WHERE content_sessionId = :sessionId) " +
            "THEN 0 ELSE 1 END WHERE content_sessionId = content_sessionId"
    )
    suspend fun updateLastComment(sessionId: Long)

    @Transaction
    suspend fun updateLastComment(sessionId: List<Long>) {
        sessionId.forEach { updateLastComment(it) }
    }

}