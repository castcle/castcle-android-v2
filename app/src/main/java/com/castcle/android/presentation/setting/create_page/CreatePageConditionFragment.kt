package com.castcle.android.presentation.setting.create_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.FragmentPageConditionBinding
import com.castcle.android.presentation.setting.create_page.item_page_condition.ItemPageConditionViewEntity
import com.castcle.android.presentation.setting.create_page.item_page_condition.ItemPageConditionViewRenderer
import com.castcle.android.presentation.sign_up.entity.ProfileBundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
//  Created by sklim on 12/9/2022 AD at 09:59.

class CreatePageConditionFragment : BaseFragment(), CreatePageConditionListener {

    private val binding by lazy {
        FragmentPageConditionBinding.inflate(layoutInflater)
    }

    private val directions = CreatePageConditionFragmentDirections

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ItemPageConditionViewRenderer())
        }
    }

    private val _itemPageCondition = MutableStateFlow(ItemPageConditionViewEntity())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initViewProperties() {
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_new_profile_bar_page,
        )
        binding.recyclerView.adapter = adapter
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            _itemPageCondition.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    override fun onCreatePageClick() {
        directions.toCreateNewProfileFragment(
            ProfileBundle.CreatePage()
        ).navigate()
    }

}
