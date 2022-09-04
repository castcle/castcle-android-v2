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

package com.castcle.android.domain.notification.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.castcle.android.core.constants.TABLE_NOTIFICATION_BADGES
import com.castcle.android.data.notification.entity.NotificationBadgesResponse

@Entity(tableName = TABLE_NOTIFICATION_BADGES)
data class NotificationBadgesEntity(
    @PrimaryKey val id: Long = 0,
    val page: Int = 0,
    val profile: Int = 0,
    val system: Int = 0,
) {

    fun total() = page.plus(profile).plus(system)

    companion object {
        fun map(response: NotificationBadgesResponse?) = NotificationBadgesEntity(
            page = response?.page ?: 0,
            profile = response?.profile ?: 0,
            system = response?.system ?: 0,
        )
    }

}