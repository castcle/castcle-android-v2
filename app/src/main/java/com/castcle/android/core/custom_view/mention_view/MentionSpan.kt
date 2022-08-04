package com.castcle.android.core.custom_view.mention_view

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

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
//  Created by sklim on 15/9/2021 AD at 08:01.

abstract class MentionSpan(
    private val normalTextColor: Int,
    private val pressedTextColor: Int,
    private val underlineEnabled: Boolean
) : ClickableSpan() {

    private var pressed = false
    override fun updateDrawState(paint: TextPaint) {
        // Determine whether to paint it pressed or normally
        val textColor = if (pressed) pressedTextColor else normalTextColor
        paint.color = textColor
        paint.isUnderlineText = underlineEnabled
        paint.bgColor = Color.TRANSPARENT
    }

    fun setPressed(value: Boolean) {
        pressed = value
    }
}