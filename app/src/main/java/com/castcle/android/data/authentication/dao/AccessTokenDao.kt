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

package com.castcle.android.data.authentication.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_ACCESS_TOKEN
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessTokenDao {

    @Query("SELECT * FROM $TABLE_ACCESS_TOKEN")
    suspend fun get(): AccessTokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AccessTokenEntity)

    @Query("SELECT * FROM $TABLE_ACCESS_TOKEN")
    fun retrieve(): Flow<AccessTokenEntity?>

}