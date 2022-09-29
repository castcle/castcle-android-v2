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

package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_boost_ad_status

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAdInformationBinding
import com.castcle.android.domain.ads.type.AdBoostStatusType
import com.castcle.android.domain.ads.type.AdvertiseType
import com.castcle.android.presentation.setting.ads.ads_manage.getAdStatusColor
import com.castcle.android.presentation.setting.ads.boost_ads.BoostAdsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ItemAdInformationViewHolder(
    private val binding: ItemAdInformationBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: BoostAdsListener,
) : CastcleViewHolder<ItemAdInformationViewEntity>(binding.root) {

    override var item = ItemAdInformationViewEntity()

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun bind(bindItem: ItemAdInformationViewEntity) {
        with(binding) {
            bindItem.advertiseEntity?.let { adsData ->
                tvCpmValue.text = "$ ${adsData.dailyBudget}"
                tvBudgetSpent.text = "$ ${adsData.dailyBudget}"
                tvCampaignCode.text = adsData.campaignCode
                tvCampaignName.text = adsData.campaignName
                tvEndDate.text = adsData.endedAt?.toFormatAdsDate()?.uppercase()
                tvStartDate.text = adsData.createdAt?.toFormatAdsDate()?.uppercase()
                tvDateCreated.text = adsData.createdAt?.toFormatAdsDate()?.uppercase()
                tvDailyBudget.text = "$${adsData.dailyBudget}/ day}"

                tvBootStatus.text = adsData.boostStatus.name
                viewBootStatus.backgroundTintList = colorStateList(
                    getAdStatusColor(adsData.boostStatus.id)
                )

                tvImpression.text = adsData.statisticsImpressionOrganic
                    ?.plus(adsData.statisticsImpressionPaid ?: 0)?.toString()
                compositeDisposable += tvCpmTitle.onClick {
                    tvBudgetSentInfo.visibleOrGone(!tvBudgetSentInfo.isVisible)
                }
                when (adsData.boostType) {
                    AdvertiseType.Content -> {
                        tvOverview.text = adsData.campaignName
                        ivPreview.gone()
                        bindItem.castEntity?.let { it ->
                            ivPreview.run {
                                visibleOrGone(it.image.isNotEmpty())
                                loadScaleCenterCrop(
                                    scale = 16 to 9,
                                    url = it.image.firstOrNull()?.original ?: "",
                                    thumbnailUrl = it.image.firstOrNull()?.thumbnail ?: ""
                                )
                            }
                        }
                        bindItem.userEntity?.let { it ->
                            ivAvatar.loadAvatarImage(it.avatar.original)
                            tvUserName.text = it.displayName
                            tvDataTime.text = adsData.createdAt?.toFormatDate()
                            tvOverview.text = adsData.campaignName
                            tvStatusFollow.visibleOrGone(false)
                        }
                    }
                    AdvertiseType.User -> {
                        bindItem.userEntity?.let { it ->
                            ivPreview.loadScaleCenterCrop(
                                scale = 12 to 10,
                                url = it.cover?.original ?: "",
                                thumbnailUrl = it.cover?.thumbnail ?: ""
                            )
                            ivAvatar.loadAvatarImage(it.avatar.original)
                            tvUserName.text = it.displayName
                            tvDataTime.text = adsData.createdAt?.toFormatDate()
                            tvOverview.text = adsData.campaignName
                            tvStatusFollow.visibleOrGone(false)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}