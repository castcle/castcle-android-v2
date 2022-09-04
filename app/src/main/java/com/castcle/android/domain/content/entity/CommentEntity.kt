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

package com.castcle.android.domain.content.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_COMMENT
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.content.entity.CommentResponse

@Entity(tableName = TABLE_COMMENT)
data class CommentEntity(
    @ColumnInfo(name = "${TABLE_COMMENT}_authorId") val authorId: String = "",
    @ColumnInfo(name = "${TABLE_COMMENT}_createdAt") val createdAt: Long = 0L,
    @ColumnInfo(name = "${TABLE_COMMENT}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_COMMENT}_isOwner") val isOwner: Boolean = false,
    @ColumnInfo(name = "${TABLE_COMMENT}_likeCount") val likeCount: Int = 0,
    @ColumnInfo(name = "${TABLE_COMMENT}_liked") val liked: Boolean = false,
    @ColumnInfo(name = "${TABLE_COMMENT}_message") val message: String = "",
) {

    companion object {
        fun map(ownerUserId: List<String?>?, response: List<CommentResponse>?) =
            response.orEmpty().map { map(ownerUserId, it) }.toMutableList()

        fun map(ownerUserId: List<String?>?, response: CommentResponse?) = CommentEntity(
            authorId = response?.authorId ?: response?.author.orEmpty(),
            createdAt = response?.createdAt?.toMilliSecond() ?: 0L,
            id = response?.id.orEmpty(),
            isOwner = ownerUserId.orEmpty()
                .filterNotNullOrBlank()
                .contains(response?.authorId ?: response?.author),
            liked = response?.participate?.liked ?: false,
            likeCount = response?.metrics?.likeCount ?: 0,
            message = response?.message.orEmpty(),
        )
    }

}