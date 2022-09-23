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

package com.castcle.android.data.ads.mapper

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.extensions.mapObject
import com.castcle.android.data.ads.entity.AdvertiseListResponse
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.ads.entity.AdvertiseEntity
import com.castcle.android.domain.ads.entity.AdvertiseListEntity
import com.castcle.android.domain.ads.type.AdvertiseType
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class AdvertiseResponseMapper {

    data class AdvertiseResponseResult(
        val advertiseList: List<AdvertiseListEntity>,
        val advertisesEntity: List<AdvertiseEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        adResponse: BaseResponse<List<AdvertiseListResponse>>?,
        ownerUser: List<UserEntity>,
    ): AdvertiseResponseResult {
        val ownerUserId = ownerUser.map { it.id }

        val userItems = adResponse?.includes
            ?.users
            ?.map { UserEntity.map(ownerUserId, it) }
            ?.toMutableList()
            ?: mutableListOf()

        return AdvertiseResponseResult(
            advertiseList = adResponse?.payload?.map {
                it.toAdvertisesListTable()
            } ?: emptyList(),

            advertisesEntity = adResponse?.payload?.let {
                AdvertiseEntity.map(it)
            } ?: emptyList(),

            user = userItems
        )
    }

    fun applySingle(
        adResponse: AdvertiseListResponse?,
        ownerUser: List<UserEntity>,
    ): AdvertiseResponseResult {
        val ownerUserId = ownerUser.map { it.id }

        val userItems = if (adResponse?.boostType == AdvertiseType.User.id) {
            listOf(
                UserEntity.map(
                    ownerUserId,
                    mapObject<UserResponse>(adResponse.payload)
                )
            )
        } else {
            listOf()
        }

        return AdvertiseResponseResult(
            advertiseList = adResponse?.let {
                listOf(it.toAdvertisesListTable())
            } ?: emptyList(),

            advertisesEntity = adResponse?.let {
                listOf(AdvertiseEntity.map(it))
            } ?: emptyList(),

            user = userItems
        )
    }

    private fun AdvertiseListResponse.toAdvertisesListTable(): AdvertiseListEntity {
        return AdvertiseListEntity(
            advertiseReferenceId = id ?: "",
            createdAt = createdAt ?: "",
            updatedAt = updatedAt ?: "",
        ).apply {
            when (boostType) {
                AdvertiseType.Content.id -> {
                    mapObject<CastResponse>(payload)?.also {
                        castContentReferenceId = it.id.orEmpty()
                        userReferenceId = it.authorId.orEmpty()
                    }
                }
                AdvertiseType.User.id -> {
                    mapObject<UserResponse>(payload)?.also {
                        userReferenceId = it.id ?: ""
                    }
                }
            }
        }
    }

}