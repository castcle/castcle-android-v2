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

package com.castcle.android.domain.ads

import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.ads.entity.AdvertiseEntityWithContent
import com.castcle.android.domain.ads.entity.BoostAdRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface AdvertiseRepository {
    suspend fun fetchAdvertiseHistory(filter: String): Flow<BaseUiState<Nothing>>
    suspend fun getAdvertiseHistory(): Flow<List<AdvertiseEntityWithContent>>
    suspend fun getAdvertiseDetail(adsId: String): Flow<AdvertiseEntityWithContent>
    suspend fun onRefreshAdvertiseHistory(): Job
    suspend fun createBoostPage(boostAdRequest: BoostAdRequest): Flow<BaseUiState<Nothing>>
    suspend fun runningAds(adsId: String): Flow<BaseUiState<Nothing>>
    suspend fun pauseAds(adsId: String): Flow<BaseUiState<Nothing>>
    suspend fun endAds(adsId: String): Flow<BaseUiState<Nothing>>
    suspend fun cancelAds(adsId: String): Flow<BaseUiState<Nothing>>
}