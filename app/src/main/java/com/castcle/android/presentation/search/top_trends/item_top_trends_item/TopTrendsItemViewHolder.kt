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

package com.castcle.android.presentation.search.top_trends.item_top_trends_item

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemTopTrendsItemBinding
import com.castcle.android.presentation.search.top_trends.TopTrendsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class TopTrendsItemViewHolder(
    private val binding: ItemTopTrendsItemBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: TopTrendsListener,
) : CastcleViewHolder<TopTrendsItemViewEntity>(binding.root) {

    override var item = TopTrendsItemViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onTrendClicked(item.trend.slug)
        }
    }

    override fun bind(bindItem: TopTrendsItemViewEntity) {
        binding.tvName.text = item.trend.name
        binding.tvRank.text = context().getString(R.string.rank_trending, item.trend.rank)
        binding.tvCount.text = context().getString(R.string.cast_count, item.trend.count.asCount())
    }

}