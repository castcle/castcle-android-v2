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

package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.*
import com.castcle.android.data.feed.entity.FeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {

    @GET("v2/feeds/recent/forYou")
    suspend fun getFeed(
        @Query(PARAMETER_MAX_RESULTS) maxResults: Int,
        @Query(PARAMETER_NEXT_TOKEN) nextToken: String?,
        @Query(PARAMETER_USER_FIELDS) userField: String = PARAMETER_USER_FIELDS_DEFAULT,
    ): Response<BaseResponse<List<FeedResponse>>>

}