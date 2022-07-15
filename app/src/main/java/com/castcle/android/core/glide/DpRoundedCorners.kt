package com.castcle.android.core.glide

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DimenRes
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.castcle.android.core.extensions.convertDpToPx
import java.nio.charset.Charset
import java.security.MessageDigest

class DpRoundedCorners(
    val context: Context,
    val viewSizePx: Int,
    @DimenRes val cornersSizeId: Int = com.intuit.sdp.R.dimen._16sdp,
    val enableTopLeft: Boolean = true,
    val enableTopRight: Boolean = true,
    val enableBottomLeft: Boolean = true,
    val enableBottomRight: Boolean = true,
) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, original: Bitmap, w: Int, h: Int): Bitmap {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val cornersSize = context.resources.getDimensionPixelSize(cornersSizeId)
        val ratio = screenWidth.toFloat().div(cornersSize)
        val cornersSizeDp = ratio.div(viewSizePx).times(original.width)
        val cornersSizePx = convertDpToPx(context, cornersSizeDp)
        return TransformationUtils.roundedCorners(
            pool,
            original,
            if (enableTopLeft) cornersSizePx else 0F,
            if (enableTopRight) cornersSizePx else 0F,
            if (enableBottomRight) cornersSizePx else 0F,
            if (enableBottomLeft) cornersSizePx else 0F,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other is DpRoundedCorners) {
            return context == context &&
                viewSizePx == other.viewSizePx &&
                cornersSizeId == other.cornersSizeId &&
                enableTopLeft == other.enableTopLeft &&
                enableTopRight == other.enableTopRight &&
                enableBottomLeft == other.enableBottomLeft &&
                enableBottomRight == other.enableBottomRight
        }
        return false
    }

    private val id = DpRoundedCorners::class.java.simpleName

    override fun hashCode() = id.hashCode()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray(Charset.forName("UTF-8")))
    }

}