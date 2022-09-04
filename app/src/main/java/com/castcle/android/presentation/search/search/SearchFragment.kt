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

package com.castcle.android.presentation.search.search

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.databinding.FragmentSearchBinding
import com.castcle.android.domain.search.type.SearchType
import com.castcle.android.domain.search.type.SearchType.*
import com.castcle.android.presentation.search.search_result.SearchResultFragment
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment() {

    private val viewModel by stateViewModel<SearchViewModel> { parametersOf(sessionId) }

    private val args by navArgs<SearchFragmentArgs>()

    private val sessionId = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateRecentSearch(args.keyword)
        viewModel.updateSearchKeyword(args.keyword)
    }

    override fun initViewProperties() {
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = SearchViewPagerAdapter(tabItemType)
        binding.viewPager.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) = Unit
            override fun onViewDetachedFromWindow(p0: View?) {
                binding.viewPager.adapter = null
                tabLayoutMediator.detach()
            }
        })
        tabLayoutMediator.attach()
        binding.searchBar.setText(args.keyword)
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.search,
        )
    }

    override fun initListener() {
        binding.searchBar.setSearchClickedListener {
            hideKeyboard()
            binding.searchBar.clearFocus()
            viewModel.updateRecentSearch(it)
            viewModel.updateSearchKeyword(it)
        }
    }

    private val tabItemType = listOf(Trend, Lastest, Photo, People)

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            tab.text = tabItemType[index].id.replaceFirstChar { it.uppercaseChar() }
        }
    }

    private inner class SearchViewPagerAdapter(private val tabItemType: List<SearchType>) :
        FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {

        override fun getItemCount() = tabItemType.size

        override fun createFragment(position: Int): SearchResultFragment {
            return SearchResultFragment.newInstance(sessionId, tabItemType[position])
        }

    }

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}