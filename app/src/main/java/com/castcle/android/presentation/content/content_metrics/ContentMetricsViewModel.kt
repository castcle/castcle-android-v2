/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.content.content_metrics

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_SMALL_ITEM
import com.castcle.android.data.content.data_source.ContentMetricsDataSource
import com.castcle.android.presentation.content.content_metrics.item_content_metrics_dialog.ContentMetricsDialogViewEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ContentMetricsViewModel(
    private val api: ContentApi,
    private val type: ContentMetricsType,
) : BaseViewModel() {

    val views: Flow<PagingData<CastcleViewEntity>> = Pager(
        config = PagingConfig(
            pageSize = PARAMETER_MAX_RESULTS_SMALL_ITEM,
        ), pagingSourceFactory = {
            ContentMetricsDataSource(
                api = api,
                contentId = type.contentId,
                type = type,
            )
        }
    ).flow.cachedIn(viewModelScope).map { pagingData ->
        pagingData.map { ContentMetricsDialogViewEntity(it) }
    }

}