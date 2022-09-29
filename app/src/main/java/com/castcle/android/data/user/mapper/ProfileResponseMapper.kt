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

package com.castcle.android.data.user.mapper

import androidx.paging.LoadType
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.ProfileType
import org.koin.core.annotation.Factory

@Factory
class ProfileResponseMapper {

    data class ProfileResponseResult(
        val cast: List<CastEntity>,
        val profile: List<ProfileEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        currentUser: UserEntity,
        sessionId: Long,
        loadType: LoadType,
        ownerUserId: List<String>,
        profileResponse: BaseResponse<List<CastResponse>>?
    ): ProfileResponseResult {
        val profileItem = if (loadType == LoadType.REFRESH) {
            val item = ProfileEntity(
                createdAt = Long.MAX_VALUE,
                originalUserId = currentUser.id,
                sessionId = sessionId,
                type = ProfileType.Profile,
            )
            listOf(item)
        } else {
            listOf()
        }
        val newCastItems = if (loadType == LoadType.REFRESH && currentUser.isOwner) {
            val item = ProfileEntity(
                createdAt = Long.MAX_VALUE.minus(1),
                originalUserId = currentUser.id,
                sessionId = sessionId,
                type = ProfileType.NewCast,
            )
            listOf(item)
        } else {
            listOf()
        }
        val userItems = profileResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val castItems = profileResponse?.includes
            ?.casts
            ?.map { CastEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()
        val feedItems = profileResponse?.payload.orEmpty().map { response ->
            val cast = CastEntity.map(ownerUserId, response).also(castItems::add)
            val referencedCast = castItems.find { it.id == response.referencedCasts?.id }
            ProfileEntity(
                createdAt = cast.createdAt.toMilliSecond() ?: 0L,
                originalCastId = cast.id,
                originalUserId = cast.authorId,
                referenceCastId = referencedCast?.id,
                referenceUserId = referencedCast?.authorId,
                sessionId = sessionId,
                type = ProfileType.Content,
            )
        }
        return ProfileResponseResult(
            cast = castItems.distinctBy { it.id },
            profile = profileItem.plus(newCastItems).plus(feedItems),
            user = userItems.distinctBy { it.id },
        )
    }

}