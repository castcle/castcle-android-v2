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

package com.castcle.android.domain.metadata.entity

import android.os.Parcelable
import androidx.room.*
import com.castcle.android.core.constants.TABLE_COUNTRY_CODE
import com.castcle.android.data.metadata.entity.CountryCodeResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_COUNTRY_CODE)
@Parcelize
data class CountryCodeEntity(
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_code", defaultValue = "")
    val code: String = "TH",
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_dialCode", defaultValue = "")
    @PrimaryKey
    val dialCode: String = "+66",
    @ColumnInfo(name = "${TABLE_COUNTRY_CODE}_name", defaultValue = "")
    val name: String = "Thailand",
) : Parcelable {

    companion object {
        fun map(response: List<CountryCodeResponse>?) = response.orEmpty().map {
            CountryCodeEntity(
                code = it.code.orEmpty(),
                dialCode = it.dialCode.orEmpty(),
                name = it.name.orEmpty(),
            )
        }
    }

}