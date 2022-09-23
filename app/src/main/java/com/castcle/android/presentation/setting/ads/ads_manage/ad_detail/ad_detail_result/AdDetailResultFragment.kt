package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.lifecycle.*
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.databinding.LayoutRecyclerStaticViewBinding
import com.castcle.android.domain.ads.entity.AdsDetailType
import com.castcle.android.domain.ads.type.AdBoostStatusType
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.EndAdStatusDialogFragment.Companion.BOOST_STATUS_CANCEL
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.EndAdStatusDialogFragment.Companion.BOOST_STATUS_END
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.change_ad_status.ChangeAdsStatusDialogFragment.Companion.BOOST_AD_STATUS_CHANGE
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_ad_report.ItemAdReportViewRenderer
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_ad_setting.ItemAdSettingViewRenderer
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.item_boost_ad_status.ItemAdInformationViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.BoostAdsListener
import io.reactivex.rxkotlin.plusAssign
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
//  Created by sklim on 21/9/2022 AD at 16:26.

class AdDetailResultFragment : BaseFragment(), BoostAdsListener {

    private val viewModel by viewModel<AdDetailResultViewModel> { parametersOf(adsId, type) }

    private val directions = AdDetailResultFragmentDirections

    private val adsId by lazy {
        arguments?.getString(ADS_ID) ?: ""
    }

    private val type by lazy {
        arguments?.getParcelable<AdsDetailType>(TYPE) ?: SearchType.Trend
    }

    private val binding by lazy {
        LayoutRecyclerStaticViewBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ItemAdInformationViewRenderer())
            registerRenderer(ItemAdReportViewRenderer())
            registerRenderer(ItemAdSettingViewRenderer())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun initConsumer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewItem.collectLatest {
                    adapter.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    handlerUiState(it)
                }
            }
        }
    }

    private fun handlerUiState(uiState: BaseUiState<Nothing>?) {
        when (uiState) {
            is BaseUiState.Loading -> {
                if (uiState.isLoading) {
                    showLoading()
                } else {
                    dismissLoading()
                }
            }
            else -> Unit
        }
    }

    override fun initViewProperties() {
        with(binding) {
            actionBar.gone()
            clToolBarCast.gone()
            if (type != AdsDetailType.Setting) {
                btBoostPage.gone()
            }


            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
            compositeDisposable += binding.btBoostPage.onClick {
                openDialogEditStatus(true)
            }
        }
    }

    override fun initListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            BOOST_STATUS_END,
            viewLifecycleOwner
        ) { _, bundle ->
            val adStatus = bundle.getString(BOOST_STATUS_CANCEL, "")
            val boostStatus = bundle.getString(BOOST_STATUS_END, "")

            if (adStatus.isNotBlank()) {
                viewModel.cancelAds()
            }
            if (boostStatus.isNotBlank()) {
                viewModel.endBoostAds()
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            BOOST_AD_STATUS_CHANGE,
            viewLifecycleOwner
        ) { _, bundle ->
            val boostStatus =
                if (Build.VERSION.SDK_INT >= 33) {
                    bundle.getParcelable(BOOST_AD_STATUS_CHANGE, AdBoostStatusType::class.java)
                } else {
                    bundle.getParcelable(BOOST_AD_STATUS_CHANGE)
                }

            when (boostStatus) {
                AdBoostStatusType.End -> {
                    openDialogEditStatus()
                }
                AdBoostStatusType.Pause -> viewModel.pauseBoostAds()
                AdBoostStatusType.Running -> viewModel.runningBoostAds()
                else -> Unit
            }
        }
        super.initListener()
    }

    override fun onBoostStatusClick(status: AdBoostStatusType) {
        directions.toAdBoostStatus(status).navigate()
    }

    private fun openDialogEditStatus(isCancel: Boolean = false) {
        EndAdStatusDialogFragment
            .newInstance(isCancel)
            .show(childFragmentManager, "")
    }

    override fun onShowCancel(isShow: Boolean) {
        binding.btBoostPage.run {
            visibleOrGone(isShow)
            text = string(R.string.fragment_ad_detail_cancel_ad)
            setTintColor(R.color.yellow_cancel)
        }
    }

    companion object {
        private const val TYPE = "TYPE"
        private const val ADS_ID = "ads_id"
        fun newInstance(type: AdsDetailType, adsId: String) = AdDetailResultFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TYPE, type)
                putString(ADS_ID, adsId)
            }
        }
    }
}