package com.castcle.android.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getSystemService
import com.castcle.android.R
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 5/9/2022 AD at 16:16.

fun View.enabled() {
    this.isEnabled = true
}

fun View.disabled() {
    this.isEnabled = false
}

fun View.visibleOrGone(visible: Boolean, delay: Long) {
    if (delay <= 0L) {
        visibleOrGone(visible)
    } else {
        postDelayed({ visibleOrGone(visible) }, delay)
    }
}

fun View.visibleOrGone(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) visible() else invisible()
}


@SuppressLint("UseCompatTextViewDrawableApis")
fun TextView.setStatePass(isPassword: Boolean = false) {
    if (isPassword) {
        context.getColorResStateList(R.color.blue)
    } else {
        context.getColorResStateList(R.color.gray_3)
    }?.run {
        compoundDrawableTintList = this
        foregroundTintList = this
        setTextColor(this)
    }
}

fun AppCompatButton.setStatePass(isPassword: Boolean = false) {
    if (isPassword) {
        context.getColorResStateList(R.color.blue)
    } else {
        context.getColorResStateList(R.color.gray_3)
    }?.run {
        backgroundTintList = this
    }
}

fun Context.openEditBirthDate(dob: String? = null, onSelect: (Date) -> Unit) {
    val date = Calendar.getInstance().time
    SingleDateAndTimePickerDialog.Builder(this).apply {
        bottomSheet()
        curved()
        backgroundColor(this@openEditBirthDate.getColorResource(R.color.black_background_3))
        mainColor(this@openEditBirthDate.getColorResource(R.color.blue))
        displayMinutes(false)
        displayHours(false)
        displayDays(false)
        displayMonth(true)
        displayYears(true)
        displayDaysOfMonth(true)
        customLocale(Locale.getDefault())
        setTimeZone(TimeZone.getDefault())
        defaultDate(dob?.dobToDate() ?: date)
        displayListener {
        }
        listener {
            onSelect(it)
        }
    }.run {
        display()
    }
}

@SuppressLint("UseCompatTextViewDrawableApis")
fun TextView.setStateDone(isPassword: Boolean = false) {
    if (isPassword) {
        context.getColorResStateList(R.color.blue)
    } else {
        context.getColorResStateList(R.color.gray_3)
    }?.run {
        backgroundTintList = this
        foregroundTintList = this
    }
}

fun TextView.setTextColorState(isPassword: Boolean = false) {
    if (isPassword) {
        context.getColorResStateList(R.color.blue)
    } else {
        context.getColorResStateList(R.color.gray_3)
    }?.run(::setTextColor)
}

fun TextView.setColorWaring(context: Context, isSelected: Boolean) {
    setTextColor(
        if (isSelected) {
            context.getColorResource(R.color.red_3)
        } else {
            context.getColorResource(R.color.blue)
        }
    )
}

fun View.setTintColor(tintColor: Int) {
    context.getColorResStateList(tintColor).run {
        backgroundTintList = this
        foregroundTintList = this
    }
}
