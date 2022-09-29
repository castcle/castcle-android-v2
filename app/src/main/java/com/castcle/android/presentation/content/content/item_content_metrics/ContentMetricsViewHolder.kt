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

package com.castcle.android.presentation.content.content.item_content_metrics

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemContentMetricsBinding
import com.castcle.android.presentation.content.content.ContentListener
import com.castcle.android.presentation.content.content_metrics.ContentMetricsType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ContentMetricsViewHolder(
    private val binding: ItemContentMetricsBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ContentListener,
) : CastcleViewHolder<ContentMetricsViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.tvLikeCount.onClick {
            listener.onContentMetricsClicked(ContentMetricsType.Like(item.contentId))
        }
        compositeDisposable += binding.tvRecastCount.onClick {
            listener.onContentMetricsClicked(ContentMetricsType.Recast(item.contentId))
        }
        compositeDisposable += binding.tvQuoteCastCount.onClick {
            listener.onQuoteCastCountClicked(contentId = item.contentId)
        }
    }

    override var item = ContentMetricsViewEntity()

    override fun bind(bindItem: ContentMetricsViewEntity) {
        binding.tvLikeCount.isVisible = item.likeCount > 0
        binding.tvLikeCount.text = context().getString(
            R.string.like_count,
            item.likeCount.asComma()
        )
        binding.tvRecastCount.isVisible = item.recastCount > 0
        binding.tvRecastCount.text = context().getString(
            R.string.recast_count,
            item.recastCount.asComma()
        )
        binding.tvQuoteCastCount.isVisible = item.quoteCount > 0
        binding.tvQuoteCastCount.text = context().getString(
            R.string.quote_cast_count,
            item.quoteCount.asComma()
        )
    }

}