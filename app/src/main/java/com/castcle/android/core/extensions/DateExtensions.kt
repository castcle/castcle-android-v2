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

package com.castcle.android.core.extensions

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.toTime(): Date {
    return try {
        val formatter = SimpleDateFormat(COMMON_DATE_FORMAT)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(this) ?: Date()
    } catch (exception: Exception) {
        Timber.e(exception)
        Date()
    }
}

fun Long.toWalletTime(): String {
    return try {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK).format(Date(this))
    } catch (exception: Exception) {
        Timber.e(exception)
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToTime(time: Long, pattern: String = SOURCE_DATE_FORMAT): String {
    val date = Date(time)
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}

const val COMMON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val SOURCE_DATE_FORMAT = "MMM dd,yyyy"