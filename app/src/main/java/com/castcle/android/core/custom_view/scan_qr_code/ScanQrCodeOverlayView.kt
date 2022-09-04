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