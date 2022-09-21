package com.castcle.android.domain.ads.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_BOOST_ADS
import com.castcle.android.domain.ads.type.ObjectiveType
import com.castcle.android.domain.ads.type.PaymentType

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
//  Created by sklim on 15/9/2022 AD at 12:26.


@Entity(tableName = TABLE_BOOST_ADS)
data class BoostAdsEntity(
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_id", defaultValue = "0")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_objective")
    val objective: ObjectiveType = ObjectiveType.Engagement,
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_payment")
    val paymentMethod: PaymentType = PaymentType.TokenBalance,
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_createAt", defaultValue = "0")
    val createAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_relationId", defaultValue = "")
    val relationId: String = "",
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_castRelationId")
    val castRelationId: String? = null,
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_campaignName", defaultValue = "")
    val campaignName: String = "",
    @ColumnInfo(name = "${TABLE_BOOST_ADS}_campaignMessage")
    val campaignMessage: String? = null,
)