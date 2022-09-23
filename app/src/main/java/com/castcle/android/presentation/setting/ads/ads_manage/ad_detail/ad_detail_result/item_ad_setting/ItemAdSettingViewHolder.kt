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

package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_ad_setting

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAdSettingBinding
import com.castcle.android.domain.ads.type.*
import com.castcle.android.presentation.setting.ads.ads_manage.getAdStatusColor
import com.castcle.android.presentation.setting.ads.boost_ads.BoostAdsListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ItemAdSettingViewHolder(
    private val binding: ItemAdSettingBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: BoostAdsListener,
) : CastcleViewHolder<ItemAdSettingViewEntity>(binding.root) {

    override var item = ItemAdSettingViewEntity()

    init {
        compositeDisposable += binding.clBoostStatus.onClick {
            if (item.advertiseEntity?.boostStatus == AdBoostStatusType.Running ||
                item.advertiseEntity?.boostStatus == AdBoostStatusType.Pause
            ) {
                listener.onBoostStatusClick(
                    item.advertiseEntity?.boostStatus ?: AdBoostStatusType.Unknown
                )
            }
        }
    }

    @SuppressLint("StringFormatMatches", "SetTextI18n")
    override fun bind(bindItem: ItemAdSettingViewEntity) {
        with(binding) {
            listener.onShowCancel(item.advertiseEntity?.adStatus == AdStatusType.InProcessing &&
                item.advertiseEntity?.boostStatus != AdBoostStatusType.End)

            item.advertiseEntity?.let {
                tvAdStatus.text = it.adStatus.name
                tvAdStatusReason.visibleOrGone(it.statusReason?.isNotBlank() == true)
                groupReason.visibleOrGone(it.statusReason?.isNotBlank() == true)

                with(itemObjective) {
                    val objective = ObjectiveType.getFromId(it.objective)
                    ivAvatar.loadImage(objective.icon)
                    tvObjectiveName.text = string(objective.name)
                    tvObjectiveDetail.text = string(objective.message)
                    tvStatusProcess.invisible()
                }

                groupSuggestion.visibleOrGone(false)
                itemCampaign.run {
                    etCampaignName.setText(it.campaignName)
                    etCampaignName.isEnabled = false
                    etMessage.setText(it.campaignMessage)
                    etMessage.isEnabled = false
                }
                if (it.adStatus == AdStatusType.InProcessing &&
                    it.boostStatus != AdBoostStatusType.End
                ) {
                    listener
                }

                tvBootStatus.text = it.boostStatus.name

                if (it.boostStatus == AdBoostStatusType.End ||
                    it.boostStatus == AdBoostStatusType.Unknown ||
                    it.adStatus == AdStatusType.Canceled
                ) {
                    ivDropDown.gone()
                }
                tvAdStatus.backgroundTintList = colorStateList(
                    getAdStatusColor(it.adStatus.id)
                )

                viewBootStatus.backgroundTintList = colorStateList(
                    getAdStatusColor(it.boostStatus.id)
                )

                with(itemBudget) {
                    val sliderEditable = false
                    val sliderColor = colorStateList(
                        if (sliderEditable) R.color.blue else R.color.gray_1
                    ) ?: ColorStateList.valueOf(Color.WHITE)

                    onBindDailyBudGet(it.dailyBudget?.toFloat() ?: DEFAULT_VALUE)
                    slDailyBudget.isEnabled = sliderEditable
                    slDailyBudget.trackActiveTintList = sliderColor
                    slDailyBudget.thumbTintList = sliderColor
                    slDailyBudget.value = it.dailyBudget?.toInt()?.toFloat() ?: DEFAULT_VALUE
                    onBindDurationBudGet(it.duration?.toFloat() ?: DEFAULT_VALUE)
                    slDuration.isEnabled = sliderEditable
                    slDuration.trackActiveTintList = sliderColor
                    slDuration.thumbTintList = sliderColor
                    slDuration.value = it.duration?.toFloat() ?: DEFAULT_VALUE
                }
            }

            item.userEntity?.let {
                with(itemPageBoost) {
                    clAdPagePage.visible()
                    ivAvatar.loadAvatarImage(it.avatar.original)
                    tvCastcleName.text = it.displayName
                    tvCastcleId.text = it.castcleId
                    tvStatusProcess.invisible()
                }
            }
            item.walletBalanceEntity?.let {
                with(itemPayment.itemTokenWallet) {
                    clTokenWallet.visibleOrGone(
                        PaymentType.getFromId(bindItem.advertiseEntity?.paymentMethod) ==
                            PaymentType.TokenBalance
                    )
                    tvStatusProcess.invisible()
                    tvBalanceValue.text = "${it.availableBalance.ifBlank { 0.0 }} CAST"
                }

                with(itemPayment.itemAdCredit) {
                    clAdCredit.visibleOrGone(
                        PaymentType.getFromId(bindItem.advertiseEntity?.paymentMethod) ==
                            PaymentType.CreditBalance
                    )
                    tvStatusProcess.invisible()
                    tvBalanceValue.text = "${it.adCredit.ifBlank { 0.0 }} CAST"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onBindDailyBudGet(value: Float) {
        with(binding.itemBudget) {
            tvDailyBugGetValue.text = "$ ${value.toInt()}"
            etDailyBugGetValue.setText(value.toInt().toString())
        }
    }

    private fun onBindDurationBudGet(value: Float) {
        with(binding.itemBudget) {
            tvDurationValue.text = string(R.string.ad_duration_value)
                .format(value.toInt())
        }
    }
}

private const val DEFAULT_VALUE = 10f