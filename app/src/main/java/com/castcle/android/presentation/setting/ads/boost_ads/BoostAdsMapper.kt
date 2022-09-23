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

package com.castcle.android.presentation.setting.ads.boost_ads

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.ads.entity.BoostAdsWithResultEntity
import com.castcle.android.domain.ads.type.*
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletBalanceEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_ad_content.ItemAdContentViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_budget.ItemBudgetViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_campaign.ItemCampaignViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_objective.ChooseObjectiveViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_page_boost.ItemChoosePageViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.item_choose_payment.ItemChoosePaymentViewEntity
import org.koin.core.annotation.Factory

@Factory
class BoostAdsMapper {

    fun apply(
        boostAdsEntity: BoostAdsWithResultEntity,
        iteView: List<CastcleViewEntity>?,
        advertiseType: AdvertiseType
    ): List<CastcleViewEntity> {
        val listView = mutableListOf<CastcleViewEntity>()
        val accountItems = ItemChoosePageViewEntity(
            user = boostAdsEntity.user ?: UserEntity(),
        )

        listView.apply {
            if (advertiseType == AdvertiseType.User) {
                add(accountItems)
            }

            add(
                ChooseObjectiveViewEntity(
                    objective = boostAdsEntity.balance?.objective ?: ObjectiveType.Engagement,
                    uniqueId = boostAdsEntity.balance?.objective?.id ?: ObjectiveType.Engagement.id
                )
            )

            add(
                iteView?.filterIsInstance<ItemCampaignViewEntity>()?.let {
                    if (it.isNotEmpty()) {
                        val itemCampaign = it.first()
                        ItemCampaignViewEntity(
                            campaignName = itemCampaign.campaignName,
                            campaignMessage = itemCampaign.campaignMessage
                        )
                    } else {
                        ItemCampaignViewEntity(
                            campaignName = boostAdsEntity.balance?.campaignName ?: "",
                            campaignMessage = boostAdsEntity.balance?.campaignMessage
                        )
                    }
                } ?: ItemCampaignViewEntity(
                    campaignName = boostAdsEntity.balance?.campaignName ?: "",
                    campaignMessage = boostAdsEntity.balance?.campaignMessage
                )
            )

            add(
                iteView?.filterIsInstance<ItemBudgetViewEntity>()?.let {
                    if (it.isNotEmpty()) {
                        val itemBudget = it.first()
                        ItemBudgetViewEntity(
                            dailyBudget = itemBudget.dailyBudget,
                            durationDay = itemBudget.durationDay,
                        )
                    } else {
                        ItemBudgetViewEntity()
                    }
                } ?: ItemBudgetViewEntity()
            )
            add(
                ItemChoosePaymentViewEntity(
                    paymentType = boostAdsEntity.balance?.paymentMethod ?: PaymentType.TokenBalance,
                    wallet = boostAdsEntity.wallet ?: WalletBalanceEntity(),
                    uniqueId = boostAdsEntity.balance?.paymentMethod?.id
                        ?: PaymentType.TokenBalance.id
                )
            )
        }

        return listView
    }

    fun applyPreview(castResult: CastWithUserEntity): CastcleViewEntity {
        return when {
            castResult.cast.image.isNotEmpty() ->
                listOf(
                    FeedImageViewEntity(
                        cast = castResult.cast,
                        feedId = castResult.cast.id,
                        uniqueId = castResult.cast.id,
                        user = castResult.user ?: UserEntity(),
                    )
                )
            castResult.cast.linkPreview.isNotBlank() -> listOf(
                FeedWebImageViewEntity(
                    cast = castResult.cast,
                    feedId = castResult.cast.id,
                    uniqueId = castResult.cast.id,
                    user = castResult.user ?: UserEntity()
                )
            )
            castResult.cast.linkUrl.isNotBlank() -> listOf(
                FeedWebViewEntity(
                    cast = castResult.cast,
                    feedId = castResult.cast.id,
                    uniqueId = castResult.cast.id,
                    user = castResult.user ?: UserEntity()
                )
            )
            else -> listOf(
                FeedTextViewEntity(
                    cast = castResult.cast,
                    feedId = castResult.cast.id,
                    uniqueId = castResult.cast.id,
                    user = castResult.user ?: UserEntity(),
                )
            )
        }.map {
            ItemAdContentViewEntity(reference = it)
        }.first()
    }

}