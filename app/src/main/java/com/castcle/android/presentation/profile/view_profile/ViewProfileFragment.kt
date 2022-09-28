package com.castcle.android.presentation.profile.view_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.openUrl
import com.castcle.android.core.extensions.visible
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.profile.view_profile.item_view_page.ItemVIewPageViewRenderer
import com.castcle.android.presentation.profile.view_profile.item_view_user.ItemVIewUserViewRenderer
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
//  Created by sklim on 28/9/2022 AD at 15:47.

class ViewProfileFragment : BaseFragment(), ViewProfileListener {

    private val viewModel by viewModel<ViewProfileViewModel> { parametersOf(userEntity) }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    private val args by navArgs<ViewProfileFragmentArgs>()

    private val userEntity: UserEntity
        get() = args.userEntity

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ItemVIewUserViewRenderer())
            registerRenderer(ItemVIewPageViewRenderer())
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
                    if (it != null) {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun initViewProperties() {
        binding.actionBar.run {
            visible()
            bind(leftButtonAction = {
                backPress()
            }, title = userEntity.displayName)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        viewModel.fetchItemView()
    }

    override fun onSocialLinkClick(link: String) {
        openUrl(link)
    }
}