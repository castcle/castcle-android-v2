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

package com.castcle.android.domain.cast.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.core.extensions.filterNotNullOrBlank
import com.castcle.android.data.cast.entity.CastResponse
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.core.entity.ImageEntity

@Entity(tableName = TABLE_CAST)
data class CastEntity(
    @ColumnInfo(name = "${TABLE_CAST}_authorId") val authorId: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_commentCount") val commentCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_commented") val commented: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_createdAt") val createdAt: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_farmCount") val farmCount: Double = 0.0,
    @ColumnInfo(name = "${TABLE_CAST}_farming") val farming: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_image") val image: List<ImageEntity> = listOf(),
    @ColumnInfo(name = "${TABLE_CAST}_isOwner") val isOwner: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_likeCount") val likeCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_liked") val liked: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_linkDescription") val linkDescription: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkPreview") val linkPreview: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkTitle") val linkTitle: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkType") val linkType: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_linkUrl") val linkUrl: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_message") val message: String = "",
    @ColumnInfo(name = "${TABLE_CAST}_quoteCount") val quoteCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_quoted") val quoted: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_recastCount") val recastCount: Int = 0,
    @ColumnInfo(name = "${TABLE_CAST}_recasted") val recasted: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_referenceCastId") val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_CAST}_reported") val reported: Boolean = false,
    @ColumnInfo(name = "${TABLE_CAST}_type") val type: CastType = CastType.Short,
    @ColumnInfo(name = "${TABLE_CAST}_updatedAt") val updatedAt: String = "",
) {

    companion object {
        fun map(ownerUserId: String?, response: CastResponse?) = map(listOf(ownerUserId), response)

        fun map(ownerUserId: List<String?>?, response: List<CastResponse>?) = response
            .orEmpty()
            .map { map(ownerUserId, it) }
            .toMutableList()

        fun map(ownerUserId: List<String?>?, response: CastResponse?) = CastEntity(
            authorId = response?.authorId.orEmpty(),
            commentCount = response?.metrics?.commentCount ?: 0,
            commented = response?.participate?.commented ?: false,
            createdAt = response?.createdAt.orEmpty(),
            farmCount = response?.metrics?.farmCount ?: 0.0,
            farming = response?.participate?.farming ?: false,
            id = response?.id.orEmpty(),
            image = response?.photo?.contents.orEmpty().mapNotNull { ImageEntity.map(it) },
            isOwner = ownerUserId.orEmpty()
                .filterNotNullOrBlank()
                .contains(response?.authorId),
            likeCount = response?.metrics?.likeCount ?: 0,
            liked = response?.participate?.liked ?: false,
            linkDescription = response?.link?.firstOrNull()?.description.orEmpty(),
            linkPreview = response?.link?.firstOrNull()?.imagePreview.orEmpty(),
            linkTitle = response?.link?.firstOrNull()?.title.orEmpty(),
            linkType = response?.link?.firstOrNull()?.type.orEmpty(),
            linkUrl = response?.link?.firstOrNull()?.url.orEmpty(),
            message = response?.message.orEmpty(),
            quoteCount = response?.metrics?.quoteCount ?: 0,
            quoted = response?.participate?.quoted ?: false,
            recastCount = response?.metrics?.recastCount ?: 0,
            recasted = response?.participate?.recasted ?: false,
            referenceCastId = response?.referencedCasts?.id,
            reported = response?.participate?.reported ?: false,
            type = CastType.getFromId(response?.referencedCasts?.type ?: response?.type),
            updatedAt = response?.updatedAt.orEmpty(),
        )
    }

}