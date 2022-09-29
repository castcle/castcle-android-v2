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

package com.castcle.android.core.custom_view.scan_qr_code

import android.content.Context
import android.graphics.*
import android.graphics.PorterDuff.Mode.CLEAR
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import kotlin.math.min

class ScanQrCodeOverlayView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val backgroundColor = ColorUtils.setAlphaComponent(Color.GRAY, 150)

    private val lineCorner = context.resources.getDimension(com.intuit.sdp.R.dimen._16sdp)

    private val lineLength by lazy {
        min(width, height).div(2).times(0.75).toFloat()
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    private val lineRect by lazy {
        RectF().apply {
            set(
                width.div(2).minus(lineLength),
                height.div(2).minus(lineLength),
                width.div(2).plus(lineLength),
                height.div(2).plus(lineLength),
            )
        }
    }

    private val lineWidth = context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp)

    private val maskBitmap by lazy {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    private val maskCanvas by lazy {
        Canvas(maskBitmap)
    }

    private val maskPaint = Paint().apply {
        alpha = 150
    }

    private val previewCorner = lineCorner.minus(lineWidth)

    private val previewPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(CLEAR)
    }

    private val previewRect by lazy {
        RectF().apply {
            set(
                lineRect.left.plus(lineWidth),
                lineRect.top.plus(lineWidth),
                lineRect.right.minus(lineWidth),
                lineRect.bottom.minus(lineWidth),
            )
        }
    }

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        maskCanvas.drawColor(backgroundColor)
        maskCanvas.drawRoundRect(lineRect, lineCorner, lineCorner, linePaint)
        maskCanvas.drawRoundRect(previewRect, previewCorner, previewCorner, previewPaint)
        canvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint)
        super.onDraw(canvas)
    }

}