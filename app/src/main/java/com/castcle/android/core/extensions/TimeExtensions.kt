package com.castcle.android.core.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String?.toMilliSecond(): Long {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(this ?: return 0L)?.time ?: 0L
    } catch (exception: Exception) {
        0L
    }
}