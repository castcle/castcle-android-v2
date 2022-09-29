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

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.SyncSocialWithUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncSocialDao {

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL WHERE syncSocial_id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL WHERE syncSocial_userId IN (:userId)")
    suspend fun deleteByUserId(userId: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SyncSocialEntity>)

    @Query("SELECT * FROM $TABLE_SYNC_SOCIAL WHERE syncSocial_userId = :userId")
    fun retrieve(userId: String): Flow<List<SyncSocialEntity>>

    @Query("SELECT * FROM $TABLE_SYNC_SOCIAL WHERE syncSocial_id = :id")
    @Transaction
    fun retrieveWithUser(id: String): Flow<SyncSocialWithUserEntity?>

    @Query("UPDATE $TABLE_SYNC_SOCIAL SET syncSocial_autoPost = :enable WHERE syncSocial_id = :id")
    suspend fun updateAutoPost(enable: Boolean, id: String)

}