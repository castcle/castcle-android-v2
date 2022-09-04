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

package com.castcle.android.data.user.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.ProfileWithResultEntity
import com.castcle.android.domain.user.type.ProfileType

@Dao
interface ProfileDao {

    @Query("DELETE FROM $TABLE_PROFILE")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_originalCastId = :castId")
    suspend fun deleteByOriginalCast(castId: String)

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_referenceCastId = :castId")
    suspend fun deleteByReferenceCast(castId: String)

    @Query("SELECT DISTINCT profile_sessionId FROM $TABLE_PROFILE WHERE profile_originalUserId = :userId AND profile_type = :type")
    suspend fun getExistSessionIdByUserId(userId: String, type: ProfileType): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ProfileEntity>)

    @Query("SELECT * FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId ORDER BY profile_createdAt DESC")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, ProfileWithResultEntity>

    @Transaction
    suspend fun replace(sessionId: Long, items: List<ProfileEntity>) {
        delete(sessionId)
        insert(items)
    }

    @Query("UPDATE $TABLE_PROFILE SET profile_ignoreReportContentId = :ignoreReportContentId WHERE profile_id = :id")
    suspend fun updateIgnoreReportContentId(id: String, ignoreReportContentId: List<String>)

}