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
import android.os.Build
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ThaiBuddhistDate
import java.time.format.DateTimeFormatter
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

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun String.dobToDate(): Date? {
    if (this.isEmpty()) {
        return null
    }
    return try {
        val formatter = SimpleDateFormat(COMMON_DATE_FORMAT)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(this)
    } catch (e: Exception) {
        val formatter = SimpleDateFormat(COMMON_DOB_DATE_FORMAT, Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(this)
    }
}

@SuppressLint("SimpleDateFormat")
fun String?.toISO8601(): String? {
    return if (isNullOrBlank()) {
        null
    } else {
        try {
            SimpleDateFormat(COMMON_DOB_DATE_FORMAT, Locale.US)
                .parse(this)
                .toFormatString(COMMON_DOB_DATE_FORMAT_V1)
        } catch (e: Exception) {
            null
        }
    }
}

fun Date?.toFormatString(
    pattern: String,
    language: String = LANGUAGE_CODE_EN,
    isHourOfDay: Boolean = false
): String {
    if (this == null) return ""
    val dateTime = if (language == LANGUAGE_CODE_TH) {
        toBuddhistDateTime(isHourOfDay)
    } else {
        toLocalDateTime(isHourOfDay)
    }
    return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale(language)))
}

fun Date.toBuddhistDateTime(isHourOfDay: Boolean): ChronoLocalDateTime<*> {
    with(Calendar.getInstance()) {
        time = this@toBuddhistDateTime
        val buddhistDate = ThaiBuddhistDate.of(
            get(Calendar.YEAR) + ADDITIONAL_BUDDHIST_YEARS,
            get(Calendar.MONTH) + 1,
            get(Calendar.DAY_OF_MONTH)
        )
        val localTime = LocalTime.of(
            if (isHourOfDay) get(Calendar.HOUR_OF_DAY) else get(Calendar.HOUR),
            get(Calendar.MINUTE),
            get(Calendar.SECOND)
        )
        return buddhistDate.atTime(localTime)
    }
}

fun Date.toLocalDateTime(isHourOfDay: Boolean): ChronoLocalDateTime<*> {
    with(Calendar.getInstance()) {
        time = this@toLocalDateTime
        return LocalDateTime.of(
            get(Calendar.YEAR),
            get(Calendar.MONTH) + 1,
            get(Calendar.DAY_OF_MONTH),
            if (isHourOfDay) get(Calendar.HOUR_OF_DAY) else get(Calendar.HOUR),
            get(Calendar.MINUTE),
            get(Calendar.SECOND)
        )
    }
}

@SuppressLint("WeekBasedYear", "SimpleDateFormat")
fun String.toFormatDateBirthDate(language: String = LANGUAGE_CODE_EN): String {
    return if (isNotBlank()) {
        try {
            SimpleDateFormat(COMMON_DATE_FORMAT)
                .parse(this).toFormatString(COMMON_DOB_DATE_FORMAT, language)
        } catch (e: Exception) {
            try {
                SimpleDateFormat(COMMON_DOB_DATE_FORMAT_V1)
                    .parse(this).toFormatString(COMMON_DOB_DATE_FORMAT, language)
            } catch (e: Exception) {
                this
            }
        }
    } else {
        "DD/MM/YY"
    }
}

const val COMMON_DOB_DATE_FORMAT_V1 = "yyyy-MM-dd"
const val LANGUAGE_CODE_TH = "th"
const val LANGUAGE_CODE_EN = "en"
const val COMMON_DOB_DATE_FORMAT = "d MMMM yyyy"
const val COMMON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val SOURCE_DATE_FORMAT = "MMM dd,yyyy"
const val ADDITIONAL_BUDDHIST_YEARS = 543