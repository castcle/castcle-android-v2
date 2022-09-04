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

package com.castcle.android.domain.authentication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.auth0.android.jwt.JWT
import com.castcle.android.core.constants.TABLE_ACCESS_TOKEN
import com.castcle.android.data.authentication.entity.GetGuestAccessTokenResponse
import com.castcle.android.data.authentication.entity.LoginResponse
import com.castcle.android.domain.authentication.type.AccessTokenType

@Entity(tableName = TABLE_ACCESS_TOKEN)
data class AccessTokenEntity(
    val accessToken: String = "",
    @PrimaryKey val id: Long = 0,
    val refreshToken: String = "",
    val type: AccessTokenType = AccessTokenType.Guest
) {

    fun getAccountId(): String {
        return try {
            JWT(accessToken).claims["id"]?.asString() ?: ""
        } catch (exception: Exception) {
            ""
        }
    }

    fun isGuest() = type is AccessTokenType.Guest

    companion object {
        fun map(response: GetGuestAccessTokenResponse?) = AccessTokenEntity(
            accessToken = response?.accessToken.orEmpty(),
            refreshToken = response?.refreshToken.orEmpty(),
            type = AccessTokenType.Guest,
        )

        fun map(response: LoginResponse?, type: AccessTokenType? = null) = AccessTokenEntity(
            accessToken = response?.accessToken.orEmpty(),
            refreshToken = response?.refreshToken.orEmpty(),
            type = type ?: AccessTokenType.Member,
        )
    }

}