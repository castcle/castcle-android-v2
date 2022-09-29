package com.castcle.android.presentation.setting.ads.ads_manage.ad_dialog_filter

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.domain.ads.type.AdFilterType
import com.castcle.android.presentation.setting.ads.ads_manage.ad_dialog_filter.item_ad_filter.AdDialogFilterViewEntity
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
//  Created by sklim on 16/9/2022 AD at 17:02.

@KoinViewModel
class AdFilterViewModel : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    fun getItems(currentFilter: AdFilterType) {
        launch(onSuccess = {
            views.postValue(it)
        }) {
            listOf(
                AdFilterType.All,
                AdFilterType.Today,
                AdFilterType.ThisWeek,
                AdFilterType.ThisMonth,
                AdFilterType.Cancel
            ).map {
                AdDialogFilterViewEntity(
                    selected = currentFilter == it,
                    filter = it,
                    uniqueId = it.filter,
                )
            }
        }
    }

}