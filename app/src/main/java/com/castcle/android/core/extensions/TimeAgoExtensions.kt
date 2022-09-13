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

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.perfomer.blitz.getTimeAgo
import java.util.*

fun TextView.setCustomTimeAgo(date: Date) {
    setCustomTimeAgo(date.time)
}

fun TextView.setCustomTimeAgo(time: Long) {

    val tag = getTag(com.perfomer.blitz.R.id.blitz)
    val stateChangeListener: BlitzAttachCusListener

    if (tag is BlitzAttachCusListener) {
        stateChangeListener = tag
    } else {
        stateChangeListener = BlitzAttachCusListener(this)
        addOnAttachStateChangeListener(stateChangeListener)
        setTag(com.perfomer.blitz.R.id.blitz, stateChangeListener)
    }

    stateChangeListener.showSeconds = false
    stateChangeListener.time = time
}

fun Context.getTimeAgoCus(time: Long, showSeconds: Boolean = false): String {
    return resources.getTimeAgoCus(time, showSeconds)
}

fun Resources.getTimeAgoCus(time: Long, showSeconds: Boolean = false): String {
    val diff = System.currentTimeMillis() - time
    return getBlitzTimeCus(diff).getText(this, diff, showSeconds, time)
}

internal fun getBlitzTimeCus(diff: Long): BlitzTimeCustom {
    val timeUnits = BlitzTimeCustom.values()
    return timeUnits.find { diff < it.differenceMs } ?: timeUnits.last()
}

internal fun BlitzTimeCustom.getText(
    resources: Resources,
    diff: Long,
    showSeconds: Boolean,
    time: Long
): String {
    return when {
        dividerMs == null -> {
            resources.getString(singleStringResource!!)
        }
        fullDay -> {
            convertLongToTime(time)
        }
        else -> {
            if (fullDay) {
                convertLongToTime(time)
            } else if (this == BlitzTimeCustom.SECONDS && !showSeconds) {
                resources.getString(com.perfomer.blitz.R.string.blitz_now)
            } else {
                val quantity = (diff / dividerMs).toInt()
                resources.getQuantityString(pluralResource!!, quantity, quantity)
            }
        }
    }
}

internal class BlitzAttachCusListener(
    private val target: TextView
) : View.OnAttachStateChangeListener {

    internal var time: Long = 0
        set(value) {
            if (field == value) return
            field = value
            updateRunnableSubscription()
        }

    internal var showSeconds: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            updateRunnableSubscription()
        }

    private val runnable = object : Runnable {
        override fun run() {
            val diff = System.currentTimeMillis() - time
            val blitzTime = getBlitzTimeCus(diff)

            target.diffedValue = blitzTime.getText(target.resources, diff, showSeconds, time)
            target.postDelayed(this, blitzTime.updateRateMs)
        }
    }

    override fun onViewDetachedFromWindow(v: View) {
        target.removeCallbacks(runnable)
    }

    override fun onViewAttachedToWindow(v: View) {
        updateRunnableSubscription()
    }

    fun dropCounter() {
        target.removeCallbacks(runnable)
    }

    private fun updateRunnableSubscription() {
        target.removeCallbacks(runnable)
        target.post(runnable)
    }

}

internal var TextView.diffedValue: String
    get() = text.toString()
    set(value) {
        if (text.toString() == value) return
        text = value
    }

private const val SECOND_MILLIS = 1000L
private const val MINUTE_MILLIS = 60L * SECOND_MILLIS
private const val HOUR_MILLIS = 60L * MINUTE_MILLIS
private const val DAY_MILLIS = 24L * HOUR_MILLIS
private const val WEEK_MILLIS = 7L * DAY_MILLIS
private const val MONTH_MILLIS = 4L * WEEK_MILLIS
private const val YEAR_MILLIS = 12L * MONTH_MILLIS

internal enum class BlitzTimeCustom(
    val dividerMs: Long? = null,
    val fullDay: Boolean = false,
    val differenceMs: Long,
    val updateRateMs: Long,
    @PluralsRes val pluralResource: Int? = null,
    @StringRes val singleStringResource: Int? = null
) {

    FUTURE(
        differenceMs = 0L,
        updateRateMs = 5L * SECOND_MILLIS,
        singleStringResource = com.perfomer.blitz.R.string.blitz_future
    ),

    SECONDS(
        dividerMs = SECOND_MILLIS,
        differenceMs = MINUTE_MILLIS,
        updateRateMs = SECOND_MILLIS,
        pluralResource = com.perfomer.blitz.R.plurals.blitz_seconds
    ),

    MINUTE(
        differenceMs = 2L * MINUTE_MILLIS,
        updateRateMs = 30L * SECOND_MILLIS,
        singleStringResource = com.perfomer.blitz.R.string.blitz_single_minute
    ),

    MINUTES(
        dividerMs = MINUTE_MILLIS,
        differenceMs = HOUR_MILLIS,
        updateRateMs = MINUTE_MILLIS,
        pluralResource = com.perfomer.blitz.R.plurals.blitz_minutes
    ),

    HOUR(
        differenceMs = 2L * HOUR_MILLIS,
        updateRateMs = 30L * MINUTE_MILLIS,
        singleStringResource = com.perfomer.blitz.R.string.blitz_single_hour
    ),

    HOURS(
        dividerMs = HOUR_MILLIS,
        differenceMs = DAY_MILLIS,
        updateRateMs = HOUR_MILLIS,
        pluralResource = com.perfomer.blitz.R.plurals.blitz_hours
    ),

    FULL_DATE(
        dividerMs = DAY_MILLIS,
        fullDay = true,
        differenceMs = DAY_MILLIS,
        updateRateMs = 12L * HOUR_MILLIS,
        singleStringResource = com.perfomer.blitz.R.string.blitz_single_day
    )
}
