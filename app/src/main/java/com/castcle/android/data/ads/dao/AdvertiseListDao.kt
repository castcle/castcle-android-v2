package com.castcle.android.data.ads.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_ADVERTISE_LIST
import com.castcle.android.domain.ads.entity.AdvertiseEntityWithContent
import com.castcle.android.domain.ads.entity.AdvertiseListEntity
import kotlinx.coroutines.flow.Flow

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 15/9/2022 AD at 14:22.

@Dao
interface AdvertiseListDao {

    @Query("DELETE FROM $TABLE_ADVERTISE_LIST")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<AdvertiseListEntity>)

    @Query("SELECT * FROM $TABLE_ADVERTISE_LIST ORDER BY createdAt DESC")
    @Transaction
    fun retrieve(): Flow<List<AdvertiseEntityWithContent>>

    @Query("SELECT * FROM $TABLE_ADVERTISE_LIST WHERE advertiseList_advertiseReferenceId =:adsId")
    @Transaction
    fun retrieveByAdsId(adsId: String): Flow<AdvertiseEntityWithContent>
}