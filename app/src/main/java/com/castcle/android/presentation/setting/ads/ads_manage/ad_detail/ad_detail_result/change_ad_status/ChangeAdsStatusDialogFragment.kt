package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.change_ad_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.domain.ads.type.AdBoostStatusType
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.change_ad_status.item_ads_status.ItemAdStatusViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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
//  Created by sklim on 22/9/2022 AD at 15:26.

class ChangeAdsStatusDialogFragment : BaseBottomSheetDialogFragment(), ChangeAdStatusListener {

    private val args by navArgs<ChangeAdsStatusDialogFragmentArgs>()

    private val viewModel by viewModel<ChangeAdsStatusViewModel> {
        parametersOf(args.adBoostStatus)
    }

    private val binding by lazy {
        DialogOptionBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ItemAdStatusViewRenderer())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initConsumer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itemView.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun initViewProperties() {
        binding.recyclerView.adapter = adapter
    }

    override fun onAdBoostStatusClick(adBoostStatusType: AdBoostStatusType) {
        requireActivity().supportFragmentManager.setFragmentResult(
            BOOST_AD_STATUS_CHANGE,
            bundleOf(BOOST_AD_STATUS_CHANGE to adBoostStatusType)
        )
        backPress()
    }

    companion object {
        const val BOOST_AD_STATUS_CHANGE = "BOOST_STATE_CHANGE"
    }
}