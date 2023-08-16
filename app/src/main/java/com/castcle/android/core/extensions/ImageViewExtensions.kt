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

package com.castcle.android.core.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.castcle.android.R
import com.castcle.android.core.glide.DpRoundedCorners
import com.castcle.android.core.glide.GlideUrlWithQueryParameter
import com.castcle.android.core.glide.ScaleCenterCrop
import com.castcle.android.domain.core.entity.ImageEntity

fun ImageView.loadAvatarImage(imageUrl: String?) {
    Glide.with(context).clear(this)
    Glide.with(context)
        .load(GlideUrlWithQueryParameter(imageUrl))
        .error(R.drawable.ic_avatar)
        .placeholder(R.drawable.ic_avatar)
        .circleCrop()
        .into(this)
}

fun ImageView.loadAvatarImageLocal(imageUri: Uri? = null, imageUrl: String?) {
    Glide.with(context).clear(this)
    Glide.with(context)
        .load(imageUri ?: GlideUrlWithQueryParameter(imageUrl))
        .error(R.drawable.ic_avatar)
        .placeholder(R.drawable.ic_avatar)
        .circleCrop()
        .into(this)
}

fun loadViewLargeImage(imageView: ImageView, image: ImageEntity) {
    val thumbnail = Glide.with(imageView.context)
        .asDrawable()
        .load(GlideUrlWithQueryParameter(image.thumbnail))
    val error = Glide.with(imageView.context)
        .load(R.drawable.ic_image)
    Glide.with(imageView.context)
        .load(GlideUrlWithQueryParameter(image.original))
        .thumbnail(thumbnail)
        .placeholder(R.drawable.ic_image)
        .error(error)
        .into(imageView)
}

fun ImageView.loadImage(
    @DrawableRes imageId: Int,
) {
    Glide.with(context)
        .load(imageId)
        .into(this)
}

fun ImageView.loadScaleCenterCrop(
    uri: Uri? = null,
    url: String?,
    thumbnailUrl: String?,
    scale: Pair<Int, Int>
) {
    val scaleCenterCrop = ScaleCenterCrop(
        scaleWidth = scale.first,
        scaleHeight = scale.second
    )
    val thumbnail = Glide.with(context)
        .asDrawable()
        .load(GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(scaleCenterCrop)
    val error = Glide.with(context)
        .load(R.drawable.ic_image)
        .transform(scaleCenterCrop)
    Glide.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(url))
        .thumbnail(thumbnail)
        .placeholder(R.drawable.ic_image)
        .transform(scaleCenterCrop)
        .error(error)
        .into(this)
}

fun ImageView.loadCenterCropWithRoundedCorners(
    @DrawableRes id: Int?,
    @DimenRes cornersSizeId: Int = com.intuit.sdp.R.dimen._16sdp,
    viewSizePx: Int,
    enableTopLeft: Boolean = true,
    enableTopRight: Boolean = true,
    enableBottomLeft: Boolean = true,
    enableBottomRight: Boolean = true,
) {
    val centerCrop = CenterCrop()
    val roundedCorners = DpRoundedCorners(
        context = context,
        cornersSizeId = cornersSizeId,
        viewSizePx = viewSizePx,
        enableTopLeft = enableTopLeft,
        enableTopRight = enableTopRight,
        enableBottomLeft = enableBottomLeft,
        enableBottomRight = enableBottomRight,
    )
    Glide.with(context)
        .load(id)
        .transform(centerCrop, roundedCorners)
        .into(this)
}

fun ImageView.loadCenterCropWithRoundedCorners(
    uri: Uri?,
    url: String?,
    thumbnailUrl: String?,
    viewSizeDp: Int,
    enableTopLeft: Boolean = true,
    enableTopRight: Boolean = true,
    enableBottomLeft: Boolean = true,
    enableBottomRight: Boolean = true,
) {
    val centerCrop = CenterCrop()
    val roundedCorners = DpRoundedCorners(
        context = context,
        viewSizePx = viewSizeDp,
        enableTopLeft = enableTopLeft,
        enableTopRight = enableTopRight,
        enableBottomLeft = enableBottomLeft,
        enableBottomRight = enableBottomRight,
    )
    val placeholder = Glide.with(context)
        .load(R.drawable.ic_image)
        .transform(centerCrop, roundedCorners)
    val thumbnail = Glide.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(centerCrop, roundedCorners)
        .thumbnail(placeholder)
        .error(placeholder)
    Glide.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(url))
        .transform(centerCrop, roundedCorners)
        .thumbnail(thumbnail)
        .error(placeholder)
        .into(this)
}

fun ImageView.loadScaleCenterCropWithRoundedCorners(
    @DimenRes cornersSizeId: Int = com.intuit.sdp.R.dimen._16sdp,
    uri: Uri? = null,
    url: String? = null,
    scaleHeight: Int = 9,
    scaleWidth: Int = 18,
    thumbnailUrl: String?,
    viewSizeDp: Int,
    enableTopLeft: Boolean = true,
    enableTopRight: Boolean = true,
    enableBottomLeft: Boolean = true,
    enableBottomRight: Boolean = true,
) {
    val centerCrop = ScaleCenterCrop(scaleWidth, scaleHeight)
    val roundedCorners = DpRoundedCorners(
        context = context,
        cornersSizeId = cornersSizeId,
        viewSizePx = viewSizeDp,
        enableTopLeft = enableTopLeft,
        enableTopRight = enableTopRight,
        enableBottomLeft = enableBottomLeft,
        enableBottomRight = enableBottomRight,
    )
    val placeholder = Glide.with(context)
        .load(R.drawable.ic_image)
        .transform(centerCrop, roundedCorners)
    val thumbnail = Glide.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(centerCrop, roundedCorners)
        .thumbnail(placeholder)
        .error(placeholder)
    Glide.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(url))
        .transform(centerCrop, roundedCorners)
        .thumbnail(thumbnail)
        .error(placeholder)
        .into(this)
}