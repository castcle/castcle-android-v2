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

package com.castcle.android.presentation.setting.sync_social_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.DialogBasicBinding
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.setting.sync_social_detail.item_sync_social_detail.SyncSocialDetailViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SyncSocialDetailFragment : BaseFragment(), SyncSocialDetailListener {

    private val viewModel by viewModel<SyncSocialDetailViewModel> { parametersOf(args.syncSocialId) }

    private val args by navArgs<SyncSocialDetailFragmentArgs>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
        lifecycleScope.launch {
            viewModel.syncSocialType.filterNotNull().collectLatest { type ->
                binding.actionBar.bind(
                    leftButtonAction = { backPress() },
                    title = getString(
                        R.string.fragment_sync_social_detail_title_1,
                        type.id.replaceFirstChar { it.uppercase() }
                    ),
                )
            }
        }
        lifecycleScope.launch {
            viewModel.onDisconnect.collectLatest {
                dismissLoading()
                backPress()
            }
        }
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                BasicDialog(
                    binding = DialogBasicBinding.inflate(layoutInflater),
                    button = string(R.string.ok),
                    isCancelable = false,
                    title = it.message.orEmpty(),
                ).show()
            }
        }
    }

    override fun onAutoPostClicked(enable: Boolean, userId: String) {
        showLoading()
        viewModel.updateAutoPost(enable = enable, userId = userId)
    }

    override fun onDisconnectClicked(userId: String) {
        showLoading()
        viewModel.disconnectWithSocial(userId = userId)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(SyncSocialDetailViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = binding.root

}