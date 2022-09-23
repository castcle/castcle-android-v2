package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.change_ad_status.item_ads_status

import android.annotation.SuppressLint
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAdStatusBinding
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.change_ad_status.ChangeAdStatusListener
import com.castcle.android.presentation.setting.ads.ads_manage.getAdStatusColor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 22/9/2022 AD at 16:20.

class ItemAdStatusViewHolder(
    private val binding: ItemAdStatusBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ChangeAdStatusListener,
) : CastcleViewHolder<ItemAdStatusViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onAdBoostStatusClick(item.adBoostStatusType)
        }
    }

    override var item = ItemAdStatusViewEntity()

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun bind(bindItem: ItemAdStatusViewEntity) {
        with(binding) {
            tvRunning.text = item.adBoostStatusType.name
            vBoostStatus.setTintColor(
                getAdStatusColor(
                    item.adBoostStatusType.id
                )
            )
        }
        binding.root.background = if (bindingAdapterPosition == 0) {
            drawable(R.drawable.bg_corner_top_16dp)
        } else {
            drawable(R.drawable.bg_rectangle)
        }
    }
}