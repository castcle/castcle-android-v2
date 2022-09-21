package com.castcle.android.presentation.setting.ads.ads_manage

import com.castcle.android.R
import com.castcle.android.domain.ads.entity.AdvertiseEntityWithContent
import com.castcle.android.domain.ads.type.*
import com.castcle.android.presentation.setting.ads.ads_manage.item_advertise.ItemAdvertiseViewEntity

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
//  Created by sklim on 15/9/2022 AD at 16:27.

fun AdvertiseEntityWithContent.toItemAdvertiseViewEntity(): ItemAdvertiseViewEntity {
    return ItemAdvertiseViewEntity(
        iconAdvertise = getIconAdvertise(advertisesEntity?.boostType ?: AdvertiseType.User),
        boostAdStatsColor = getAdStatusColor(
            advertisesEntity?.boostStatus?.id ?: AdBoostStatusType.Unknown.id
        ),
        adStatsColor = getAdStatusColor(
            advertisesEntity?.adStatus?.id ?: AdStatusType.InProcessing.id
        ),
        advertiseEntity = advertisesEntity,
        userEntity = user,
        uniqueId = advertisesEntity?.adsId.orEmpty()
    )
}

fun getIconAdvertise(advertiseType: AdvertiseType): Int {
    return when (advertiseType) {
        is AdvertiseType.User -> R.drawable.ic_boost_page
        is AdvertiseType.Content -> R.drawable.ic_boost_cast
    }
}

private fun getAdStatusColor(status: String?): Int {
    return when (status) {
        AdStatusType.InProcessing.id, AdBoostStatusType.Pause.id -> {
            R.color.gray_1
        }
        AdStatusType.Approved.id, AdBoostStatusType.Running.id -> {
            R.color.green_approved
        }
        AdStatusType.Declined.id, AdBoostStatusType.End.id -> {
            R.color.red_3
        }
        AdStatusType.Canceled.id -> {
            R.color.yellow_cancel
        }
        else -> R.color.gray_1
    }
}