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

package com.castcle.android.data.search.mapper

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.search.entity.SearchEntity
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class SearchResponseMapper {

    data class SearchResponseResult(
        val cast: List<CastEntity>,
        val search: List<SearchEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        searchResponse: BaseResponse<out List<Any>>?,
        ownerUserId: List<String>,
        sessionId: Long
    ): SearchResponseResult {
        val userItems = searchResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = searchResponse?.includes
            ?.casts
            ?.map { CastEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val searchItems = searchResponse?.payload.orEmpty().mapNotNull { response ->
            when (response) {
                is UserResponse -> {
                    val user = UserEntity.map(ownerUserId, response).also(userItems::add)
                    SearchEntity(
                        originalUserId = user.id,
                        sessionId = sessionId,
                    )
                }
                is CastResponse -> {
                    val cast = CastEntity.map(ownerUserId, response).also(castItems::add)
                    val referencedCast = castItems.find { it.id == response.referencedCasts?.id }
                    SearchEntity(
                        originalCastId = cast.id,
                        originalUserId = cast.authorId,
                        referenceCastId = referencedCast?.id,
                        referenceUserId = referencedCast?.authorId,
                        sessionId = sessionId,
                    )
                }
                else -> null
            }
        }
        return SearchResponseResult(
            cast = castItems.distinctBy { it.id },
            search = searchItems,
            user = userItems.distinctBy { it.id },
        )
    }

}