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

package com.castcle.android.presentation.content.content_metrics_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.DialogContentMetricsBinding
import com.castcle.android.presentation.content.content_metrics.ContentMetricsFragment
import com.castcle.android.presentation.content.content_metrics.ContentMetricsType
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContentMetricsDialogFragment : BaseBottomSheetDialogFragment() {

    private val viewModel by viewModel<ContentMetricsDialogViewModel> { parametersOf(args.type) }

    private val args by navArgs<ContentMetricsDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.viewPager.adapter = ViewPagerAdapter()
    }

    override fun initListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }
        })
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab?.position ?: 0
            }
        })
        binding.tabLayout.getTabAt(if (args.type is ContentMetricsType.Like) 0 else 1)?.select()
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.content.collectLatest {
                binding.viewPager.isUserInputEnabled = it.likeCount > 0 && it.recastCount > 0
                binding.tabLayout.isInvisible = it.likeCount <= 0 || it.recastCount <= 0
                binding.tvTitle.isVisible = it.likeCount <= 0 || it.recastCount <= 0
                binding.tvTitle.text = if (args.type is ContentMetricsType.Like) {
                    string(R.string.likes)
                } else {
                    string(R.string.recasts)
                }
            }
        }
    }

    private val binding by lazy {
        DialogContentMetricsBinding.inflate(layoutInflater)
    }

    private inner class ViewPagerAdapter : FragmentStateAdapter(childFragmentManager, lifecycle) {

        override fun getItemCount(): Int = binding.tabLayout.tabCount

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> ContentMetricsFragment.newInstance(ContentMetricsType.Like(args.type.contentId))
            else -> ContentMetricsFragment.newInstance(ContentMetricsType.Recast(args.type.contentId))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = binding.root

}