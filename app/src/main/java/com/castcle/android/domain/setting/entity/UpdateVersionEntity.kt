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

package com.castcle.android.domain.setting.entity

import androidx.room.TypeConverter
import com.castcle.android.data.setting.entity.UpdateVersionResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class UpdateVersionEntity(
    val buttonNegativeMessage: String = "",
    val buttonPositiveMessage: String = "",
    val message: String = "",
    val title: String = "",
    val updateUrl: String = "",
    val version: Int = 0,
) {

    companion object {
        fun map(response: UpdateVersionResponse?) = UpdateVersionEntity(
            buttonNegativeMessage = response?.meta?.buttonCancel?.en.orEmpty(),
            buttonPositiveMessage = response?.meta?.buttonOk?.en.orEmpty().ifBlank {
                response?.meta?.button?.en.orEmpty()
            },
            message = response?.meta?.message?.en.orEmpty(),
            title = response?.meta?.title?.en.orEmpty(),
            updateUrl = response?.android?.url.orEmpty(),
            version = response?.android?.version?.toIntOrNull() ?: 0,
        )
    }

    class Converter {

        private val type = object : TypeToken<UpdateVersionEntity>() {}.type

        @TypeConverter
        fun fromEntity(item: UpdateVersionEntity): String = Gson().toJson(item, type)

        @TypeConverter
        fun toEntity(item: String?): UpdateVersionEntity = Gson().fromJson(item, type)

    }

}