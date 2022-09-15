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

package com.castcle.android.core.glide

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DimenRes
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.castcle.android.core.extensions.convertDpToPx
import com.castcle.android.core.extensions.toInt
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
            return viewSizePx == other.viewSizePx &&
                cornersSizeId == other.cornersSizeId &&
                enableTopLeft == other.enableTopLeft &&
                enableTopRight == other.enableTopRight &&
                enableBottomLeft == other.enableBottomLeft &&
                enableBottomRight == other.enableBottomRight
        }
        return false
    }

    override fun hashCode() = DpRoundedCorners::class.java.simpleName.hashCode() +
        (viewSizePx * 123) +
        (cornersSizeId * 234) +
        (enableTopLeft.toInt() * 345) +
        (enableTopRight.toInt() * 456) +
        (enableBottomLeft.toInt() * 567) +
        (enableBottomRight.toInt() * 678)

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(hashCode().toByte())
    }

}