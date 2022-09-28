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

package com.castcle.android.data.content.data_source

import androidx.paging.*
import androidx.room.withTransaction
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.domain.content.entity.ContentQuoteCastWithResultEntity
import com.castcle.android.domain.content.mapper.ContentQuoteCastResponseMapper
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType

@ExperimentalPagingApi
class ContentQuoteCastRemoteMediator(
    private val api: ContentApi,
    private val contentId: String,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val mapper: ContentQuoteCastResponseMapper,
    private val sessionId: Long,
) : RemoteMediator<Int, ContentQuoteCastWithResultEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContentQuoteCastWithResultEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.REFRESH -> {
                    database.withTransaction { database.contentQuoteCast().delete(sessionId) }
                    null
                }
                LoadType.APPEND -> {
                    database.withTransaction { database.loadKey().get(sessionId).firstOrNull() }
                        ?.loadKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getContentQuoteCast(
                contentId = contentId,
                untilId = loadKey,
            )

            val ownerUserId = database.withTransaction {
                database.user().get().map { it.id }
            }

            val items = if (response.isSuccessful && response.body() != null) {
                mapper.apply(response.body(), ownerUserId, sessionId)
            } else {
                return MediatorResult.Error(ErrorMapper().map(response.errorBody()))
            }

            with(glidePreloader) {
                loadCast(items = items.cast)
                loadUser(items = items.user)
            }

            val nextLoadKey = LoadKeyEntity(
                loadKey = response.body()?.meta?.getNextLoadKey(currentLoadKey = loadKey),
                loadType = LoadKeyType.ContentQuoteCast,
                sessionId = sessionId,
            )

            database.withTransaction {
                database.cast().insert(items.cast)
                database.contentQuoteCast().insert(items.contentQuoteCast)
                database.loadKey().insert(nextLoadKey)
                database.user().upsert(items.user)
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: Exception) {
            MediatorResult.Error(ErrorMapper().map(exception))
        }
    }

}