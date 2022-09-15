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

package com.castcle.android.presentation.feed.item_feed_image_item

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedImageItemBinding
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedImageItemViewHolder(
    private val binding: ItemFeedImageItemBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedImageViewListener,
) : CastcleViewHolder<FeedImageItemViewEntity>(binding.root) {

    override var item = FeedImageItemViewEntity()

    init {
        compositeDisposable += binding.ivDelete.onClick {
            listener.onChildImageDeleteClicked(bindingAdapterPosition)
        }
        compositeDisposable += binding.ivImage.onClick {
            if (item.uri == null) {
                listener.onChildImageClicked(bindingAdapterPosition)
            }
        }
    }

    override fun bind(bindItem: FeedImageItemViewEntity) {
        binding.tvMoreImage.text = context().getString(R.string.more_image, item.itemCount.minus(4))
        binding.tvMoreImage.isVisible = bindingAdapterPosition == 3 && item.itemCount > 4
        binding.ivDelete.isVisible = item.uri != null
        binding.ivImage.layoutParams.cast<ConstraintLayout.LayoutParams>()?.dimensionRatio =
            if (bindingAdapterPosition == 2 && item.itemCount == 3) {
                "2:1"
            } else {
                "1:1"
            }
        binding.ivImage.imageTintList = if (bindingAdapterPosition == 3 && item.itemCount > 4) {
            colorStateList(R.color.black_transparent_1)
        } else {
            colorStateList(R.color.transparent)
        }
        when {
            bindingAdapterPosition == 0 -> binding.ivImage.loadCenterCropWithRoundedCorners(
                thumbnailUrl = item.image.thumbnail,
                uri = item.uri,
                url = item.image.original,
                viewSizeDp = if (item.itemCount == 1) {
                    screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp))
                } else {
                    screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2)
                },
                enableTopRight = item.itemCount <= 1,
                enableBottomLeft = item.itemCount <= 2,
                enableBottomRight = item.itemCount <= 1,
            )
            bindingAdapterPosition == 1 -> binding.ivImage.loadCenterCropWithRoundedCorners(
                thumbnailUrl = item.image.thumbnail,
                uri = item.uri,
                url = item.image.original,
                viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2),
                enableTopLeft = false,
                enableBottomLeft = false,
                enableBottomRight = item.itemCount <= 2,
            )
            bindingAdapterPosition == 2 && item.itemCount == 3 -> binding.ivImage.loadScaleCenterCropWithRoundedCorners(
                thumbnailUrl = item.image.thumbnail,
                uri = item.uri,
                url = item.image.original,
                viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)),
                enableTopLeft = false,
                enableTopRight = false,
            )
            bindingAdapterPosition == 2 && item.itemCount >= 4 -> binding.ivImage.loadCenterCropWithRoundedCorners(
                thumbnailUrl = item.image.thumbnail,
                uri = item.uri,
                url = item.image.original,
                viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2),
                enableTopLeft = false,
                enableTopRight = false,
                enableBottomRight = false,
            )
            bindingAdapterPosition == 3 -> binding.ivImage.loadCenterCropWithRoundedCorners(
                thumbnailUrl = item.image.thumbnail,
                uri = item.uri,
                url = item.image.original,
                viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2),
                enableTopLeft = false,
                enableTopRight = false,
                enableBottomLeft = false,
            )
        }
    }

}