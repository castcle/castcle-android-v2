package com.castcle.android.core.extensions

import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToInt

private fun AppCompatTextView.resizeDrawable(multiplyer: Double) {
    compoundDrawables.forEach {
        val drawable = it ?: return
        val pixelDrawableSize = lineHeight.times(multiplyer).roundToInt()
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
        setCompoundDrawables(drawable, null, null, null)
    }
}