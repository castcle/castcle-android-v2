package com.castcle.android.domain.ads.entity

import com.google.gson.annotations.SerializedName

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
//  Created by sklim on 20/9/2022 AD at 19:01.

data class BoostAdResponse(
    @SerializedName("adStatus")
    val adStatus: String,
    @SerializedName("boostStatus")
    val boostStatus: String,
    @SerializedName("boostType")
    val boostType: String,
    @SerializedName("campaignCode")
    val campaignCode: String,
    @SerializedName("campaignMessage")
    val campaignMessage: String,
    @SerializedName("campaignName")
    val campaignName: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("dailyBudget")
    val dailyBudget: Double,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("engagement")
    val engagement: EngagementResponse,
    @SerializedName("objective")
    val objective: String,
    @SerializedName("payload")
    val payload: Any? = null,
    @SerializedName("statistics")
    val statistics: StatisticsResponse,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class EngagementResponse(
    @SerializedName("clicks")
    val clicks: Int,
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("farm")
    val farm: Int,
    @SerializedName("followerGain")
    val followerGain: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("quotecast")
    val quotecast: Int,
    @SerializedName("recast")
    val recast: Int,
    @SerializedName("rewardDistributed")
    val rewardDistributed: Int,
    @SerializedName("saved")
    val saved: Int
)

data class StatisticsResponse(
    @SerializedName("budgetSpent")
    val budgetSpent: Double,
    @SerializedName("CPM")
    val cpm: Double,
    @SerializedName("impression")
    val impression: ImpressionResponse,
    @SerializedName("reach")
    val reach: ReachResponse
)

data class ImpressionResponse(
    @SerializedName("organic")
    val organic: Int,
    @SerializedName("paid")
    val paid: Int
)

data class ReachResponse(
    @SerializedName("organic")
    val organic: Int,
    @SerializedName("paid")
    val paid: Int
)