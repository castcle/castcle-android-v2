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

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.castcle.android.R
import kotlin.math.roundToInt

private fun AppCompatTextView.resizeDrawable(multiplyer: Double) {
    compoundDrawables.forEach {
        val drawable = it ?: return
        val pixelDrawableSize = lineHeight.times(multiplyer).roundToInt()
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
        setCompoundDrawables(drawable, null, null, null)
    }
}

fun TextView.setTintColor(tintColor: Int) {
    context.getColorResStateList(tintColor).run {
        backgroundTintList = this
        foregroundTintList = this
    }
}

fun TextView.makeSpannableString(
    message: String,
    startSp: Int,
    endSp: Int,
    onClickSpannable: (() -> Unit)? = null
) {
    val textMessage = SpannableString(message)
    val textClickListener1: ClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClickSpannable?.invoke()
        }
    }

    textMessage.setSpan(textClickListener1, startSp, endSp, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    textMessage.setSpan(
        ForegroundColorSpan(context.getColorResource(R.color.blue)),
        startSp,
        endSp,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    movementMethod = LinkMovementMethod.getInstance()
    text = textMessage
}
