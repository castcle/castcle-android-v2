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

@SuppressLint("SimpleDateFormat")
fun convertLongToTime(time: Long, pattern: String = SOURCE_DATE_FORMAT): String {
    val date = Date(time)
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}

const val FORMAT_AGO = "yyyy-MM-dd HH:mm:ss"
const val COMMON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val COMMON_DOB_DATE_FORMAT = "d MMMM yyyy"
const val COMMON_DOB_DATE_FORMAT_V1 = "yyyy-MM-dd"
const val COMMON_DOB_DATE_FORMAT_OLD = "DD MM YY"
const val DOB_DATE_FORMAT = "d/MMMM/YYYY"
const val PAGE_DATE_FORMAT = "d MMM YYYY"
const val WALLET_HISTORY_FORMAT = "dd/MM/yyyy HH:mm"
const val SOURCE_DATE_FORMAT = "MMM dd,yyyy"
const val LANGUAGE_CODE_TH = "th"
const val LANGUAGE_CODE_EN = "en"
const val ADDITIONAL_BUDDHIST_YEARS = 543