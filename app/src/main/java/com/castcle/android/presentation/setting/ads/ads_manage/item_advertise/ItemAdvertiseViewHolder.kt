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

package com.castcle.android.presentation.setting.ads.ads_manage.item_advertise

import android.annotation.SuppressLint
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemHistoryAdvertiseBinding
import com.castcle.android.domain.ads.type.AdStatusType
import com.castcle.android.presentation.setting.ads.ads_manage.AdvertiseListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ItemAdvertiseViewHolder(
    private val binding: ItemHistoryAdvertiseBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: AdvertiseListener,
) : CastcleViewHolder<ItemAdvertiseViewEntity>(binding.root) {

    override var item = ItemAdvertiseViewEntity()

    init {
        compositeDisposable += itemView.rootView.onClick {
            listener.onAdvertiseClick(item.advertiseEntity?.adsId.orEmpty())
        }
    }

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun bind(bindItem: ItemAdvertiseViewEntity) {
        with(binding) {
            ivBoostType.loadCenterCropWithRoundedCorners(
                id = bindItem.iconAdvertise,
                cornersSizeId = com.intuit.sdp.R.dimen._15sdp,
                viewSizePx = dimenPx(com.intuit.sdp.R.dimen._72sdp),
                enableTopRight = false,
                enableBottomRight = false,
            )
            bindItem.userEntity?.let {
                ivAvatar.loadAvatarImage(it.avatar.original)
            }
            bindItem.advertiseEntity?.let {
                tvCampaignName.text = it.campaignName
                tvCampaignCode.text = it.campaignCode
                tvUserName.text = it.campaignName
                tvCampaignMessage.text = it.campaignMessage
                tvTotalSpendingValue.text = "$ ${it.dailyBudget}"
                tvImpressionValue.text = binding.root.context.getString(
                    R.string.ad_impressions, "${
                        it.statisticsReachOrganic?.plus(
                            it.statisticsImpressionPaid ?: 0
                        )
                    }"
                )
                gpBoostStatus.gone()
                with(tvStatusProcess) {
                    text = it.adStatus.name
                    bindItem.adStatsColor?.let { color ->
                        setTintColor(color)
                    }
                }

                if (it.adStatus != AdStatusType.InProcessing &&
                    it.adStatus != AdStatusType.Declined &&
                    it.adStatus != AdStatusType.Canceled
                ) {
                    gpBoostStatus.visible()
                    tvBoostStatus.text = it.boostStatus.name
                    bindItem.boostAdStatsColor?.let { color ->
                        vBoostStatus.setTintColor(color)
                    }
                }
            }
        }
    }
}