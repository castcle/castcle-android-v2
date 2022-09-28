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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.content.content_metrics.ContentMetricsType

class ContentMetricsDataSource(
    private val api: ContentApi,
    private val contentId: String,
    private val type: ContentMetricsType,
) : PagingSource<String, UserEntity>() {

    override fun getRefreshKey(state: PagingState<String, UserEntity>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserEntity> {
        return try {
            val response = if (type is ContentMetricsType.Like) {
                apiCall { api.getContentLikesUsers(contentId = contentId, untilId = params.key) }
            } else {
                apiCall { api.getContentRecastsUsers(contentId = contentId, untilId = params.key) }
            }

            LoadResult.Page(
                data = UserEntity.map(listOf(), response?.payload),
                prevKey = null,
                nextKey = response?.meta?.oldestId,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}