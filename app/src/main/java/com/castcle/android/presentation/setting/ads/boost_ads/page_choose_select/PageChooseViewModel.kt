package com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select

import androidx.lifecycle.MutableLiveData
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.ads.type.*
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_choose_header.HeaderViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_object_boost.ChooseItemObjectiveViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_page_boost.ChoosePageViewEntity
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_payment.PaymentViewEntity
import org.koin.android.annotation.KoinViewModel

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
//  Created by sklim on 19/9/2022 AD at 09:57.

@KoinViewModel
class PageChooseViewModel(
    private val database: CastcleDatabase
) : BaseViewModel() {

    val itemView = MutableLiveData<List<CastcleViewEntity>>()

    fun fetchItemView(bundle: BoostAdBundle) {
        launch(
            onSuccess = {
                itemView.postValue(it)
            }
        ) {
            when (bundle) {
                is BoostAdBundle.BoostChoosePageBundle -> {
                    database.user().get(UserType.People)
                        .map {
                            listOf(
                                HeaderViewEntity(headerName = R.string.profile),
                                ChoosePageViewEntity(
                                    isSelected = it.id == bundle.userId,
                                    userEntity = it,
                                    uniqueId = it.id,
                                ),
                                HeaderViewEntity(headerName = R.string.page),
                            )
                        }.plus(
                            database.user().get(UserType.Page)
                                .map {
                                    listOf(
                                        ChoosePageViewEntity(
                                            isSelected = it.id == bundle.userId,
                                            userEntity = it,
                                            uniqueId = it.id,
                                        )
                                    )
                                }
                        ).flatten()
                }
                is BoostAdBundle.BoostChooseObjectiveBundle -> {
                    listOf(
                        ChooseItemObjectiveViewEntity(
                            isSelected = bundle.boostObjective == ObjectiveType.Engagement,
                            objectiveType = ObjectiveType.Engagement
                        ),
                        ChooseItemObjectiveViewEntity(
                            isSelected = bundle.boostObjective == ObjectiveType.Reach,
                            objectiveType = ObjectiveType.Reach
                        )
                    )
                }
                is BoostAdBundle.BoostChoosePaymentBundle -> {
                    database.walletBalance().getWalletEntity(bundle.userId)
                        ?.map {
                            listOf(
                                PaymentViewEntity(
                                    isSelected = bundle.paymentType == PaymentType.TokenBalance,
                                    tokenBalance = it.totalBalance.toDouble(),
                                    paymentType = PaymentType.TokenBalance
                                ),
                                PaymentViewEntity(
                                    isSelected = bundle.paymentType == PaymentType.CreditBalance,
                                    tokenBalance = it.adCredit.ifBlank {
                                        "0"
                                    }.toDouble(),
                                    paymentType = PaymentType.CreditBalance
                                )
                            )
                        }?.flatten() ?: listOf(
                        PaymentViewEntity(
                            isSelected = true,
                            tokenBalance = 0.0,
                            paymentType = PaymentType.TokenBalance
                        ),
                        PaymentViewEntity(
                            isSelected = false,
                            tokenBalance = 0.0,
                            paymentType = PaymentType.CreditBalance
                        )
                    )
                }
                else -> listOf(HeaderViewEntity(headerName = R.string.profile))
            }

        }
    }
}