package com.castcle.android.domain.ads.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_ADVERTISE
import com.castcle.android.data.ads.entity.AdvertiseListResponse
import com.castcle.android.domain.ads.type.*

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
//  Created by sklim on 15/9/2022 AD at 13:58.

@Entity(tableName = TABLE_ADVERTISE)
data class AdvertiseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "${TABLE_ADVERTISE}_id")
    val adsId: String? = "",
    @ColumnInfo(name = "adStatus")
    val adStatus: AdStatusType = AdStatusType.InProcessing,
    @ColumnInfo(name = "boostStatus")
    val boostStatus: AdBoostStatusType = AdBoostStatusType.Unknown,
    @ColumnInfo(name = "boostType")
    val boostType: AdvertiseType? = AdvertiseType.User,
    @ColumnInfo(name = "campaignCode")
    val campaignCode: String? = "",
    @ColumnInfo(name = "campaignMessage")
    val campaignMessage: String? = "",
    @ColumnInfo(name = "campaignName")
    val campaignName: String? = "",
    @ColumnInfo(name = "createdAt")
    val createdAt: String? = "",
    @ColumnInfo(name = "dailyBudget")
    var dailyBudget: Double? = 0.0,
    @ColumnInfo(name = "dailyBudgetValue")
    var dailyBudgetValue: Double? = 0.0,
    @ColumnInfo(name = "duration")
    var duration: Int? = 0,
    @ColumnInfo(name = "objective")
    var objective: String? = "",
    @ColumnInfo(name = "dailyBid")
    var dailyBid: DailyBidType? = DailyBidType.AutoBid,
    @ColumnInfo(name = "dailyBidValue")
    var dailyBidValue: Int? = 0,
    @ColumnInfo(name = "paymentMethod")
    var paymentMethod: String? = "",
    @ColumnInfo(name = "updatedAt")
    val updatedAt: String? = "",
    @ColumnInfo(name = "endedAt")
    val endedAt: String? = "",
    @ColumnInfo(name = "statusReason")
    val statusReason: String? = "",
    @ColumnInfo(name = "engagementClicks")
    val engagementClicks: Int? = 0,
    @ColumnInfo(name = "engagementComments")
    val engagementComments: Int? = 0,
    @ColumnInfo(name = "engagementFarm")
    val engagementFarm: Int? = 0,
    @ColumnInfo(name = "engagementFollowerGain")
    val engagementFollowerGain: Int? = 0,
    @ColumnInfo(name = "engagementLikes")
    val engagementLikes: Int? = 0,
    @ColumnInfo(name = "engagementQuotecast")
    val engagementQuotecast: Int? = 0,
    @ColumnInfo(name = "engagementRecast")
    val engagementRecast: Int? = 0,
    @ColumnInfo(name = "engagementRewardDistributed")
    val engagementRewardDistributed: Int? = 0,
    @ColumnInfo(name = "engagementSaved")
    val engagementSaved: Int? = 0,
    @ColumnInfo(name = "statisticsCpm")
    val statisticsCpm: Double? = 0.0,
    @ColumnInfo(name = "statisticsBudgetSpent")
    val statisticsBudgetSpent: Double? = 0.0,
    @ColumnInfo(name = "statisticsImpressionOrganic")
    val statisticsImpressionOrganic: Int? = 0,
    @ColumnInfo(name = "statisticsImpressionPaid")
    val statisticsImpressionPaid: Int? = 0,
    @ColumnInfo(name = "statisticsReachOrganic")
    val statisticsReachOrganic: Int? = 0,
    @ColumnInfo(name = "statisticsReachPaid")
    val statisticsReachPaid: Int? = 0
) {
    companion object {
        fun map(advertiseResponse: List<AdvertiseListResponse>): List<AdvertiseEntity> {
            return advertiseResponse.map { map(it) }
        }

        fun map(advertiseResponse: AdvertiseListResponse): AdvertiseEntity {
            return advertiseResponse.toAdvertisesTable()
        }

        private fun AdvertiseListResponse.toAdvertisesTable(): AdvertiseEntity {
            return AdvertiseEntity(
                adsId = id,
                adStatus = AdStatusType.getFromId(adStatus),
                boostStatus = AdBoostStatusType.getFromId(boostStatus),
                boostType = AdvertiseType.getFromId(boostType),
                campaignCode = campaignCode ?: "",
                campaignMessage = campaignMessage ?: "",
                campaignName = campaignName ?: "",
                createdAt = createdAt ?: "",
                dailyBudget = dailyBudget ?: 0.0,
                duration = duration ?: 0,
                engagementClicks = engagement?.clicks ?: 0,
                engagementComments = engagement?.comments ?: 0,
                engagementFarm = engagement?.farm ?: 0,
                engagementFollowerGain = engagement?.followerGain ?: 0,
                engagementLikes = engagement?.likes ?: 0,
                engagementQuotecast = engagement?.quotecast ?: 0,
                engagementRecast = engagement?.recast ?: 0,
                engagementRewardDistributed = engagement?.rewardDistributed ?: 0,
                engagementSaved = engagement?.saved ?: 0,
                objective = objective ?: "",
                statisticsCpm = statistics?.cpm ?: 0.0,
                statisticsBudgetSpent = statistics?.budgetSpent ?: 0.0,
                statisticsImpressionOrganic = statistics?.impression?.organic ?: 0,
                statisticsImpressionPaid = statistics?.impression?.paid ?: 0,
                statisticsReachOrganic = statistics?.reach?.organic ?: 0,
                statisticsReachPaid = statistics?.reach?.paid ?: 0,
                updatedAt = updatedAt ?: "",
                endedAt = endedAt ?: "",
                statusReason = statusReason ?: "",
            )
        }
    }
}