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
import com.castcle.android.core.constants.TABLE_CONTENT
import com.castcle.android.domain.content.type.ContentType

@Entity(tableName = TABLE_CONTENT)
data class ContentEntity(
    @ColumnInfo(name = "${TABLE_CONTENT}_commentId", defaultValue = "NULL")
    val commentId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_commentUserId", defaultValue = "NULL")
    val commentUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_createdAt", defaultValue = "0")
    val createdAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_id", defaultValue = "0") @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_ignoreReportContentId", defaultValue = "[]")
    val ignoreReportContentId: List<String> = listOf(),
    @ColumnInfo(name = "${TABLE_CONTENT}_isLastComment", defaultValue = "0")
    val isLastComment: Boolean = false,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalCastId", defaultValue = "NULL")
    val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_originalUserId", defaultValue = "NULL")
    val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceCastId", defaultValue = "NULL")
    val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_referenceUserId", defaultValue = "NULL")
    val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_CONTENT}_replyAt", defaultValue = "0")
    val replyAt: Long = Long.MIN_VALUE,
    @ColumnInfo(name = "${TABLE_CONTENT}_sessionId", defaultValue = "0")
    val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_CONTENT}_type", defaultValue = "")
    val type: ContentType = ContentType.Content,
) {

    fun toMetricsItem() = copy(
        createdAt = createdAt.minus(1),
        type = ContentType.Metrics,
    )

}