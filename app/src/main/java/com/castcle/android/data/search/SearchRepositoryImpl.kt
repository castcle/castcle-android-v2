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

package com.castcle.android.data.search

import com.castcle.android.core.api.SearchApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.search.mapper.SearchSuggestionResponseMapper
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.*
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class SearchRepositoryImpl(
    private val api: SearchApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val searchSuggestionResponseMapper: SearchSuggestionResponseMapper,
) : SearchRepository {

    override suspend fun getHashtags(keyword: String): List<HashtagEntity> {
        val response = apiCall { api.hashtagLookup(keyword) }
        return HashtagEntity.map(response?.payload)
    }

    override suspend fun getMentions(keyword: String): List<UserEntity> {
        val response = apiCall { api.mentionsLookup(keyword) }
        val ownerUserId = database.user().get().map { it.id }
        val user = UserEntity.map(ownerUserId, response?.payload)
        glidePreloader.loadUser(user)
        database.user().upsert(user)
        return user
    }

    override suspend fun getTopTrends(): List<SearchSuggestionHashtagEntity> {
        val response = apiCall { api.getTopTrends() }
        val ownerUserId = database.user().get().map { it.id }
        return searchSuggestionResponseMapper.apply(ownerUserId, response).hashtag
    }

    override suspend fun searchByKeyword(keyword: String): SearchSuggestionEntity {
        val response = apiCall { api.searchByKeyword(keyword) }
        val ownerUserId = database.user().get().map { it.id }
        val result = searchSuggestionResponseMapper.apply(ownerUserId, response)
        glidePreloader.loadUser(result.user)
        return result
    }

}