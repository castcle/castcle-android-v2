package com.castcle.android.presentation.setting.ads.boost_ads

import android.view.View
import com.castcle.android.core.base.recyclerview.CastcleListener
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
//  Created by sklim on 15/9/2022 AD at 09:45.

interface BoostAdsListener : CastcleListener {
    fun onChoosePageClick(userId: String) = Unit
    fun onChooseObjective(objective: ObjectiveType) = Unit
    fun onChoosePayment(userId: String, payment: PaymentType) = Unit
    fun onLimitDailyBudget() = Unit
    fun onLearnMoreClick() = Unit
    fun onHideKeyboard(view: View) = Unit
    fun onShowKeyboard(view: View) = Unit
    fun onEnableBoost(isPass: Boolean) = Unit
    fun onInputCampaignName(message: String) = Unit
    fun onInputCampaignMessage(name: String, message: String) = Unit
    fun onCampaignNameFocus() = Unit
    fun onCampaignMessageFocus() = Unit
    fun onFocusDaily() = Unit

    fun onBoostStatusClick(status: AdBoostStatusType) = Unit
    fun onShowCancel(isShow: Boolean) = Unit
}