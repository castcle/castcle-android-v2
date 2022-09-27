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

package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.data.user.entity.SocialResponse
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.user.type.SocialType

@Entity(tableName = TABLE_SYNC_SOCIAL)
data class SyncSocialEntity(
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_active") val active: Boolean = false,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_autoPost") val autoPost: Boolean = false,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_avatar") val avatar: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_displayName") val displayName: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_provider") val provider: SocialType = SocialType.Twitter,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_socialId") val socialId: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_userId") val userId: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_userName") val userName: String = "",
) {

    companion object {
        fun map(response: SocialResponse?, userId: String): SyncSocialEntity {
            return SyncSocialEntity(
                active = response?.active ?: false,
                autoPost = response?.autoPost ?: false,
                avatar = response?.avatar.orEmpty(),
                displayName = response?.displayName.orEmpty(),
                id = response?.id.orEmpty(),
                provider = SocialType.getFromId(response?.provider),
                socialId = response?.socialId.orEmpty(),
                userId = userId,
                userName = response?.username ?: response?.userName.orEmpty(),
            )
        }

        fun map(response: UserResponse?): List<SyncSocialEntity> {
            return listOfNotNull(
                response?.syncSocial?.facebook,
                response?.syncSocial?.google,
                response?.syncSocial?.twitter,
            ).map { map ->
                SyncSocialEntity(
                    active = map.active ?: false,
                    autoPost = map.autoPost ?: false,
                    avatar = map.avatar.orEmpty(),
                    displayName = map.displayName.orEmpty(),
                    id = map.id.orEmpty(),
                    provider = SocialType.getFromId(map.provider),
                    socialId = map.socialId.orEmpty(),
                    userId = response?.id.orEmpty(),
                    userName = map.username ?: map.userName.orEmpty(),
                )
            }
        }

        fun map(response: List<UserResponse>?): List<SyncSocialEntity> {
            return response.orEmpty().map { map(it) }.flatten()
        }
    }

}