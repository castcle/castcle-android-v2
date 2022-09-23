package com.castcle.android.presentation.setting.ads.ads_manage.ad_detail

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.databinding.FragmentAdDetailBinding
import com.castcle.android.domain.ads.entity.AdsDetailType
import com.castcle.android.presentation.search.search.SearchViewModel
import com.castcle.android.presentation.setting.ads.ads_manage.ad_detail.ad_detail_result.AdDetailResultFragment
import com.google.android.material.tabs.TabLayoutMediator
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
//  Created by sklim on 21/9/2022 AD at 09:25.

class AdDetailFragment : BaseFragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private val args by navArgs<AdDetailFragmentArgs>()

    private val adsId: String
        get() = args.adsId

    private val tabItemType = listOf(
        AdsDetailType.Information,
        AdsDetailType.Report,
        AdsDetailType.Setting
    )

    private val binding by lazy {
        FragmentAdDetailBinding.inflate(layoutInflater)
    }

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            tab.text = tabItemType[index].id.replaceFirstChar { it.uppercaseChar() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun initViewProperties() {
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = AdDetailPagerAdapter(tabItemType)
        binding.viewPager.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) = Unit
            override fun onViewDetachedFromWindow(p0: View) {
                binding.viewPager.adapter = null
                tabLayoutMediator.detach()
            }
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onActionBarTitle(tabItemType[position].id.replaceFirstChar { it.uppercaseChar() })
                binding.tabLayout.getTabAt(position)?.select()
            }
        })
        tabLayoutMediator.attach()
    }

    private fun onActionBarTitle(title: String) {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = title,
        )
    }

    private inner class AdDetailPagerAdapter(private val tabItemType: List<AdsDetailType>) :
        FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {

        override fun getItemCount() = tabItemType.size

        override fun createFragment(position: Int): AdDetailResultFragment {
            return AdDetailResultFragment.newInstance(tabItemType[position], adsId)
        }

    }
}