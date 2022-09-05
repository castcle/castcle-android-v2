/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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

    @Query("DELETE FROM $TABLE_CONTENT WHERE content_commentId = :commentId")
    suspend fun deleteByCommentId(commentId: String)

    @Query("DELETE FROM $TABLE_CONTENT WHERE content_createdAt = :createdAt")
    suspend fun deleteByCreatedAt(createdAt: Long)

    @Query("SELECT * FROM $TABLE_CONTENT WHERE content_commentId = :commentId")
    suspend fun get(commentId: String): ContentEntity?

    @Query("SELECT * FROM $TABLE_CONTENT WHERE content_sessionId = :sessionId AND content_type = :type")
    suspend fun get(sessionId: Long, type: ContentType): List<ContentEntity>

    @Query("SELECT * FROM $TABLE_CONTENT WHERE content_createdAt = :createdAt AND content_type = :type")
    suspend fun getByCreatedAt(createdAt: Long, type: ContentType): List<ContentEntity>

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

    @Query("UPDATE $TABLE_CONTENT SET content_ignoreReportContentId = :ignoreReportContentId WHERE content_id = :id")
    suspend fun updateIgnoreReportContentId(id: String, ignoreReportContentId: List<String>)

}