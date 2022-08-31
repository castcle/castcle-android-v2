package com.castcle.android.core.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class ScaleCenterCrop(
    private val scaleWidth: Int,
    private val scaleHeight: Int,
) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, original: Bitmap, w: Int, h: Int): Bitmap {
        var width = scaleWidth
        var height = scaleHeight
        while (width + scaleWidth < original.width && height + scaleHeight < original.height) {
            width += scaleWidth
            height += scaleHeight
        }
        return Bitmap.createBitmap(
            original,
            original.width.minus(width).div(2),
            original.height.minus(height).div(2),
            width,
            height,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other is ScaleCenterCrop) {
            return scaleWidth == other.scaleWidth &&
                scaleHeight == other.scaleHeight
        }
        return false
    }

    override fun hashCode() = ScaleCenterCrop::class.java.simpleName.hashCode() +
        (scaleWidth * 123) +
        (scaleHeight * 234)

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(hashCode().toByte())
    }

}