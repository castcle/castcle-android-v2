package com.castcle.android.domain.ads.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletBalanceEntity

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
//  Created by sklim on 15/9/2022 AD at 14:15.

data class AdvertiseEntityWithContent(
    @Embedded
    val advertiseList: AdvertiseListEntity = AdvertiseListEntity(),

    @Relation(
        parentColumn = "${TABLE_ADVERTISE_LIST}_advertiseReferenceId",
        entityColumn = "${TABLE_ADVERTISE}_id"
    )
    val advertisesEntity: AdvertiseEntity? = null,

    @Relation(
        parentColumn = "${TABLE_ADVERTISE_LIST}_userReferenceId",
        entityColumn = "${TABLE_USER}_id"
    )
    val user: UserEntity? = null,

    @Relation(
        parentColumn = "${TABLE_ADVERTISE_LIST}_castContentReferenceId",
        entityColumn = "${TABLE_CAST}_id"
    )
    val castContent: CastEntity? = null,

    @Relation(
        parentColumn = "${TABLE_ADVERTISE_LIST}_userReferenceId",
        entityColumn = "${TABLE_WALLET_BALANCE}_userId"
    )
    val walletBalanceEntity: WalletBalanceEntity? = null,
)
