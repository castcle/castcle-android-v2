package com.castcle.android.presentation.setting.ads.ads_manage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.custom_view.load_state.item_loading_append.LoadingAppendViewRenderer
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.visibleOrGone
import com.castcle.android.databinding.FragmentAdsManageBinding
import com.castcle.android.domain.ads.type.AdFilterType
import com.castcle.android.domain.ads.type.BoostAdBundle
import com.castcle.android.presentation.setting.ads.ads_manage.ad_dialog_filter.AdDialogFilterFragment.Companion.SELECT_AD_FILTER
import com.castcle.android.presentation.setting.ads.ads_manage.item_advertise.ItemAdvertiseViewRenderer
import com.castcle.android.presentation.setting.ads.ads_manage.item_empty.AdvertiseEmptyViewRenderer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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
//  Created by sklim on 14/9/2022 AD at 15:05.

class AdsManageFragment : BaseFragment(), AdvertiseListener {

    private val viewModel by viewModel<AdsManageViewModel>()

    private val directions = AdsManageFragmentDirections

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(LoadingAppendViewRenderer())
            registerRenderer(ItemAdvertiseViewRenderer())
            registerRenderer(AdvertiseEmptyViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentAdsManageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                recyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.DEFAULT,
            )
        }
        lifecycleScope.launch {
            viewModel.filter.collectLatest {
                bindAdFilter(it)
            }
        }
    }

    override fun initListener() {
        setFragmentResultListener(SELECT_AD_FILTER) { _, bundle ->
            if (Build.VERSION.SDK_INT >= 33) {
                bundle.getParcelable(SELECT_AD_FILTER, AdFilterType::class.java)?.let {
                    viewModel.filter.value = it
                }
            } else {
                bundle.getParcelable<AdFilterType>(SELECT_AD_FILTER)?.let {
                    viewModel.filter.value = it
                }
            }
        }
    }

    private fun bindAdFilter(adFilter: AdFilterType) {
        with(binding.tvHistoryFiltterStatus) {
            visibleOrGone(adFilter != AdFilterType.All)
            text = adFilter.filterName
        }
    }

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.ad_manager_title,
        )
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.refreshData()
        }
        compositeDisposable += binding.btBoostPage.onClick {
            onBoostAdsClick()
        }

        compositeDisposable += binding.ivFilter.onClick {
            onAdFilterClick(viewModel.filter.value)
        }
    }

    override fun initObserver() {
        viewModel.itemView.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        setFragmentResultListener(BOOST_SUCCESS) { _, bundle ->
            bundle.getBoolean(BOOST_SUCCESS).let {
                if (it) {
                    binding.recyclerView.smoothScrollBy(0, 0)
                }
            }
        }
    }

    override fun onAdFilterClick(filterType: AdFilterType) {
        directions.toAdDialogFilter(filterType).navigate()
    }

    override fun onAdvertiseClick(adId: String) {

    }

    override fun onBoostAdsClick() {
        directions.toBoostAdsFragment(BoostAdBundle.BoostAdPageBundle).navigate()
    }

    companion object {
        const val BOOST_SUCCESS = "boost-success"
    }
}