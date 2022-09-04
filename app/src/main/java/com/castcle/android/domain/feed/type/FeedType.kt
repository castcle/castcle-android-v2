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

package com.castcle.android.domain.feed.type

import androidx.room.TypeConverter

sealed class FeedType(val id: String) {

    object AdsContent : FeedType(id = "ads-content")

    object AdsPage : FeedType(id = "ads-page")

    object Content : FeedType(id = "content")

    object NewCast : FeedType(id = "new-cast")

    object WhoToFollow : FeedType(id = "suggestion-follow")

    companion object {
        fun getFromId(id: String?) = when (id) {
            AdsContent.id -> AdsContent
            AdsPage.id -> AdsPage
            Content.id -> Content
            WhoToFollow.id -> WhoToFollow
            else -> NewCast
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: FeedType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}