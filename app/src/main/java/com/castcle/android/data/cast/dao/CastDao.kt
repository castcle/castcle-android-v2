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

package com.castcle.android.data.cast.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.cast.type.CastType

@Dao
interface CastDao {

    @Query("UPDATE $TABLE_CAST SET casts_commentCount = casts_commentCount - 1 WHERE casts_id = :castId")
    suspend fun decreaseCommentCount(castId: String)

    @Query("UPDATE $TABLE_CAST SET casts_recastCount = casts_recastCount - 1 WHERE casts_id = :castId")
    suspend fun decreaseRecastCount(castId: String)

    @Query("DELETE FROM $TABLE_CAST")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_CAST WHERE casts_id = :castId")
    suspend fun delete(castId: String)

    @Query("SELECT * FROM $TABLE_CAST WHERE casts_id = :castId")
    suspend fun get(castId: String): CastWithUserEntity?

    @Query("SELECT * FROM $TABLE_CAST WHERE casts_referenceCastId = :referenceCastId AND casts_type = :type AND casts_authorId = :userId")
    suspend fun get(referenceCastId: String, type: CastType, userId: String): CastEntity?

    @Query("UPDATE $TABLE_CAST SET casts_commentCount = casts_commentCount + 1, casts_commented = 1 WHERE casts_id = :castId")
    suspend fun increaseCommentCount(castId: String)

    @Query("UPDATE $TABLE_CAST SET casts_quoteCount = casts_quoteCount + 1 WHERE casts_id = :castId")
    suspend fun increaseQuoteCastCount(castId: String)

    @Query("UPDATE $TABLE_CAST SET casts_recastCount = casts_recastCount + 1 WHERE casts_id = :castId")
    suspend fun increaseRecastCount(castId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CastEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: CastEntity)

    @Query("UPDATE $TABLE_CAST SET casts_recasted = :recasted WHERE casts_id = :castId")
    suspend fun updateRecasted(castId: String, recasted: Boolean)

    @Query("UPDATE $TABLE_CAST SET casts_reported = :reported WHERE casts_id = :castId")
    suspend fun updateReported(castId: String, reported: Boolean)

}