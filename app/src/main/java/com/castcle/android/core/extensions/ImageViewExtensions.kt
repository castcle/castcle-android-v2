package com.castcle.android.core.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.castcle.android.R
import com.castcle.android.core.glide.*

fun ImageView.loadAvatarImage(imageUrl: String?) {
    GlideApp.with(context).clear(this)
    GlideApp.with(context)
        .load(GlideUrlWithQueryParameter(imageUrl))
        .error(R.drawable.ic_avatar)
        .placeholder(R.drawable.ic_avatar)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(
    @DrawableRes imageId: Int,
) {
    GlideApp.with(context)
        .load(imageId)
        .into(this)
}

fun ImageView.loadScaleCenterCrop(
    url: String?,
    thumbnailUrl: String?,
    scale: Pair<Int, Int>
) {
    val scaleCenterCrop = ScaleCenterCrop(
        scaleWidth = scale.first,
        scaleHeight = scale.second
    )
    val thumbnail = GlideApp.with(context)
        .asDrawable()
        .load(GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(scaleCenterCrop)
    val error = GlideApp.with(context)
        .load(R.drawable.ic_image)
        .transform(scaleCenterCrop)
    GlideApp.with(context)
        .load(GlideUrlWithQueryParameter(url))
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
    GlideApp.with(context)
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
    val placeholder = GlideApp.with(context)
        .load(R.drawable.ic_image)
        .transform(centerCrop, roundedCorners)
    val thumbnail = GlideApp.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(centerCrop, roundedCorners)
        .thumbnail(placeholder)
        .error(placeholder)
    GlideApp.with(context)
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
    val placeholder = GlideApp.with(context)
        .load(R.drawable.ic_image)
        .transform(centerCrop, roundedCorners)
    val thumbnail = GlideApp.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(thumbnailUrl))
        .transform(centerCrop, roundedCorners)
        .thumbnail(placeholder)
        .error(placeholder)
    GlideApp.with(context)
        .load(uri ?: GlideUrlWithQueryParameter(url))
        .transform(centerCrop, roundedCorners)
        .thumbnail(thumbnail)
        .error(placeholder)
        .into(this)
}