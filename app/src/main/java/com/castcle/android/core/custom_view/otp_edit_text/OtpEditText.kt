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

package com.castcle.android.core.custom_view.otp_edit_text

import android.content.Context
import android.graphics.*
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class OtpEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private val lineGap = context.resources.getDimension(com.intuit.sdp.R.dimen._12sdp)

    private val lineHeight = context.resources.getDimension(com.intuit.sdp.R.dimen._2sdp)

    private val lineTextGap = context.resources.getDimension(com.intuit.sdp.R.dimen._12sdp)

    private val lineWidth by lazy {
        (width - (lineGap * (otpLength - 1))) / otpLength
    }

    private val numberFilter = InputFilter { it, _, _, _, _, _ ->
        if (it.matches("[0-9]+".toRegex())) {
            it
        } else {
            ""
        }
    }

    private val otpLength = filters
        .filterIsInstance<LengthFilter>()
        .let { it.firstOrNull()?.max ?: 6 }

    private val paintLine = Paint(paint).apply {
        color = Color.WHITE
        strokeWidth = lineHeight
    }

    private val paintText = paint.apply {
        color = Color.WHITE
    }

    private val viewHeight = context
        .obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textSize))
        .getDimensionPixelSize(0, -1)
        .plus(lineTextGap)
        .plus(paddingBottom)
        .plus(paddingTop)
        .toInt()

    init {
        background = null
        filters += arrayOf(LengthFilter(otpLength), numberFilter)
        inputType = InputType.TYPE_CLASS_NUMBER
        isCursorVisible = false
        movementMethod = null
    }

    override fun onDraw(canvas: Canvas) {
        repeat(otpLength) { index ->
            val linePointStartX = index * (lineWidth + lineGap)
            val linePointEndX = linePointStartX + lineWidth
            val linePointMiddleX = (linePointStartX + linePointEndX) / 2
            val linePointY = (height - paddingBottom).toFloat()
            val text = text?.getOrNull(index)?.toString().orEmpty()
            val textWidth = paintText.measureText(text)
            val textPointX = linePointMiddleX - (textWidth / 2)
            val textPointY = height - paddingBottom - lineTextGap
            canvas.drawLine(linePointStartX, linePointY, linePointEndX, linePointY, paintLine)
            canvas.drawText(text, textPointX, textPointY, paintText)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, viewHeight)
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        setSelection(text?.length ?: 0)
    }

}