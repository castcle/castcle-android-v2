package com.castcle.android.data.ads.entity

import com.castcle.android.data.user.entity.UserResponse
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
//  Created by sklim on 15/9/2022 AD at 14:07.

data class AdvertiseListResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("adStatus") val adStatus: String? = null,
    @SerializedName("boostStatus") val boostStatus: String? = null,
    @SerializedName("boostType") val boostType: String? = null,
    @SerializedName("campaignCode") val campaignCode: String? = null,
    @SerializedName("campaignMessage") val campaignMessage: String? = null,
    @SerializedName("campaignName") val campaignName: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("endedAt") val endedAt: String? = null,
    @SerializedName("dailyBudget") val dailyBudget: Double? = null,
    @SerializedName("dailyBudgetValue") val dailyBudgetValue: Double? = null,
    @SerializedName("duration") val duration: Int? = null,
    @SerializedName("engagement") val engagement: Engagement? = null,
    @SerializedName("objective") val objective: String? = null,
    @SerializedName("payload") val payload: Any? = null,
    @SerializedName("statistics") val statistics: Statistics? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("includes") val includes: IncludesUser? = null,
    @SerializedName("statusReason") val statusReason: String? = null,
)

data class IncludesUser(
    @SerializedName("users") val users: List<UserResponse>? = null,
)

data class Engagement(
    @SerializedName("clicks") val clicks: Int? = null,
    @SerializedName("comments") val comments: Int? = null,
    @SerializedName("farm") val farm: Int? = null,
    @SerializedName("followerGain") val followerGain: Int? = null,
    @SerializedName("likes") val likes: Int? = null,
    @SerializedName("quotecast") val quotecast: Int? = null,
    @SerializedName("recast") val recast: Int? = null,
    @SerializedName("rewardDistributed") val rewardDistributed: Int? = null,
    @SerializedName("saved") val saved: Int? = null,
)

data class Statistics(
    @SerializedName("budgetSpent") val budgetSpent: Double? = null,
    @SerializedName("CPM") val cpm: Double? = null,
    @SerializedName("impression") val impression: Impression? = null,
    @SerializedName("reach") val reach: Reach? = null,
)

data class Impression(
    @SerializedName("organic") val organic: Int? = null,
    @SerializedName("paid") val paid: Int? = null,
)

data class Reach(
    @SerializedName("organic") val organic: Int? = null,
    @SerializedName("paid") val paid: Int? = null,
)