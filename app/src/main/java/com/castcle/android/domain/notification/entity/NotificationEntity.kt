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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.castcle.android.core.constants.TABLE_NOTIFICATION
import com.castcle.android.data.notification.entity.NotificationResponse
import com.castcle.android.domain.notification.type.NotificationType

@Entity(tableName = TABLE_NOTIFICATION)
data class NotificationEntity(
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_actionId") val actionId: String = "",
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_image") val image: String = "",
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_isRead") val isRead: Boolean = false,
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_message") val message: String = "",
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_type") val type: NotificationType = NotificationType.Cast,
    @ColumnInfo(name = "${TABLE_NOTIFICATION}_updatedAt") val updatedAt: String = "",
) {

    companion object {
        fun map(response: NotificationResponse?) = NotificationEntity(
            actionId = response?.contentId ?: response?.profileId.orEmpty(),
            id = response?.id.orEmpty(),
            image = response?.avatar?.thumbnail.orEmpty(),
            isRead = response?.read ?: false,
            message = response?.message.orEmpty(),
            type = if (response?.contentId != null) {
                NotificationType.Cast
            } else {
                NotificationType.Follow
            },
            updatedAt = response?.updatedAt.orEmpty(),
        )
    }

}