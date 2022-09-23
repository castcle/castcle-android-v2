package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result

import androidx.lifecycle.viewModelScope
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.ads.AdvertiseRepository
import com.castcle.android.domain.ads.entity.AdsDetailType
import com.castcle.android.domain.ads.entity.AdvertiseEntityWithContent
import com.castcle.android.domain.ads.type.AdBoostStatusType
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_ad_report.ItemAdReportViewEntity
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_ad_setting.ItemAdSettingViewEntity
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_boost_ad_status.ItemAdInformationViewEntity
import com.castcle.android.presentation.setting.ads.ads_manage.getAdStatusColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
//  Created by sklim on 21/9/2022 AD at 17:00.

@KoinViewModel
class AdDetailResultViewModel(
    private val advertiseRepository: AdvertiseRepository,
    private val adDetailType: AdsDetailType,
    private val adDetailId: String
) : BaseViewModel() {

    init {
        getAdDetailViewItem()
    }

    val viewItem = MutableStateFlow<List<CastcleViewEntity>?>(null)

    val uiState = MutableStateFlow<BaseUiState<Nothing>?>(null)

    fun runningBoostAds() {
        launch {
            advertiseRepository.runningAds(adDetailId)
                .collectLatest {
                    uiState.emit(it)
                }
        }
    }

    fun pauseBoostAds() {
        launch {
            advertiseRepository.pauseAds(adDetailId)
                .collectLatest {
                    uiState.emit(it)
                }
        }
    }

    fun endBoostAds() {
        launch {
            advertiseRepository.endAds(adDetailId)
                .collectLatest {
                    uiState.emit(it)
                }
        }
    }

    fun cancelAds() {
        launch {
            advertiseRepository.cancelAds(adDetailId)
                .collectLatest {
                    uiState.emit(it)
                }
        }
    }

    private fun getAdDetailViewItem() {
        launch {
            advertiseRepository
                .getAdvertiseDetail(adDetailId)
                .collectLatest {
                    handlerViewItem(it)
                }
        }
    }

    private fun handlerViewItem(adsDetail: AdvertiseEntityWithContent) {
        when (adDetailType) {
            AdsDetailType.Information -> {
                listOf(
                    ItemAdInformationViewEntity(
                        advertiseEntity = adsDetail.advertisesEntity,
                        userEntity = adsDetail.user,
                        castEntity = adsDetail.castContent,
                        boostAdStatsColor = getAdStatusColor(
                            adsDetail.advertisesEntity?.boostStatus?.id
                                ?: AdBoostStatusType.Unknown.id
                        )
                    )
                )
            }
            AdsDetailType.Report -> {
                listOf(
                    ItemAdReportViewEntity(
                        advertiseEntity = adsDetail.advertisesEntity,
                    )
                )
            }
            AdsDetailType.Setting -> {
                listOf(
                    ItemAdSettingViewEntity(
                        advertiseEntity = adsDetail.advertisesEntity,
                        userEntity = adsDetail.user,
                        walletBalanceEntity = adsDetail.walletBalanceEntity
                    )
                )
            }
        }.also {
            viewModelScope.launch {
                viewItem.emit(it)
            }
        }
    }

}
