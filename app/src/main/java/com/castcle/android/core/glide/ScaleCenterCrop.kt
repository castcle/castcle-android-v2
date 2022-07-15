package com.castcle.android.core.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
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
            return scaleWidth == scaleWidth && scaleHeight == other.scaleHeight
        }
        return false
    }

    private val id = ScaleCenterCrop::class.java.simpleName

    override fun hashCode() = id.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray(Charset.forName("UTF-8")))
    }

}