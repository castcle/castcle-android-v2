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

package com.castcle.android.data.notification.data_source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.castcle.android.core.api.NotificationApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType
import com.castcle.android.domain.notification.entity.NotificationEntity

@ExperimentalPagingApi
class NotificationRemoteMediator(
    private val api: NotificationApi,
    private val database: CastcleDatabase,
) : RemoteMediator<Int, NotificationEntity>() {

    private val sessionId = System.currentTimeMillis()

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NotificationEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.notification().delete() }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val response = api.getNotification(
                maxResults = state.config.pageSize,
                untilId = loadKey,
            )

            val items = if (loadKey == null) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.payload?.map { NotificationEntity.map(it) }.orEmpty()
                } else {
                    return MediatorResult.Error(ErrorMapper().map(response.errorBody()))
                }
            } else {
                listOf()
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = if (loadKey == null) response.body()?.meta?.oldestId else null,
                loadType = LoadKeyType.Notification,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.loadKey().insert(nextLoadKey)
                database.notification().insert(items)
            }

            MediatorResult.Success(endOfPaginationReached = nextLoadKey.loadKey == null)
        } catch (exception: Exception) {
            MediatorResult.Error(ErrorMapper().map(exception))
        }
    }

}