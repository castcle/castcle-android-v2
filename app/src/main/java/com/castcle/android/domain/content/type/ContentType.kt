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

package com.castcle.android.domain.content.type

import androidx.room.TypeConverter

sealed class ContentType(val id: String) {

    object Comment : ContentType("comment")

    object Content : ContentType("content")

    object Metrics : ContentType("metrics")

    object Reply : ContentType("reply")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Comment.id -> Comment
            Content.id -> Content
            Metrics.id -> Metrics
            else -> Reply
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: ContentType): String = item.id

        @TypeConverter
        fun toEntity(item: String): ContentType = getFromId(item)

    }

}