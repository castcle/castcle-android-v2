package com.castcle.android.core.custom_view.mention_view

import android.text.method.LinkMovementMethod

import android.view.MotionEvent

import android.widget.TextView

import android.graphics.RectF
import android.text.*


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
//  Created by sklim on 14/9/2021 AD at 15:17.

class AccurateMovementMethod: LinkMovementMethod() {

    private var instance: AccurateMovementMethod? = null
    private val touchBounds = RectF()
    private var pressedSpan: MentionSpan? = null


    @Synchronized
    fun getInstance(): AccurateMovementMethod? {
        if (instance == null) {
            instance = AccurateMovementMethod()
        }
        return instance
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressedSpan = getMentionSpan(widget, buffer, event)
                if (pressedSpan != null) {
                    pressedSpan!!.setPressed(true)
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(pressedSpan),
                        buffer.getSpanEnd(pressedSpan)
                    )
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val pressedSpan2 = getMentionSpan(widget, buffer, event)
                if (pressedSpan != null && pressedSpan2 !== pressedSpan) {
                    pressedSpan!!.setPressed(false)
                    pressedSpan = null
                    Selection.removeSelection(buffer)
                }
            }
            else -> {
                if (pressedSpan != null) {
                    pressedSpan!!.setPressed(false)
                    super.onTouchEvent(widget, buffer, event)
                }
                pressedSpan = null
                Selection.removeSelection(buffer)
            }
        }
        return true
    }

    private fun getMentionSpan(tv: TextView, span: Spannable, e: MotionEvent): MentionSpan? {
        // Find the location in which the touch was made
        var x = e.x.toInt()
        var y = e.y.toInt()

        // Ignore padding
        x -= tv.totalPaddingLeft
        y -= tv.totalPaddingTop

        // Account for scrollable text
        x += tv.scrollX
        y += tv.scrollY
        val layout: Layout = tv.layout
        val touchedLine: Int = layout.getLineForVertical(y)
        val touchOffset: Int = layout.getOffsetForHorizontal(touchedLine, x.toFloat())

        // Set bounds of the touched line
        touchBounds.left = layout.getLineLeft(touchedLine)
        touchBounds.top = layout.getLineTop(touchedLine).toFloat()
        touchBounds.right = layout.getLineRight(touchedLine)
        touchBounds.bottom = layout.getLineBottom(touchedLine).toFloat()

        // Ensure the span falls within the bounds of the touch
        var touchSpan: MentionSpan? = null
        if (touchBounds.contains(x.toFloat(), y.toFloat())) {
            // Find clickable spans that lie under the touched area
            val spans = span.getSpans(touchOffset, touchOffset, MentionSpan::class.java)
            touchSpan = if (spans.isNotEmpty()) spans[0] else null
        }
        return touchSpan
    }
}