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