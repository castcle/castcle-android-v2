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

package com.castcle.android.presentation.wallet.wallet_edit_shortcut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.FragmentWalletEditShortcutBinding
import com.castcle.android.domain.wallet.entity.WalletShortcutEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletEditShortcutFragment : BaseFragment(), WalletEditShortcutListener {

    private val viewModel by viewModel<WalletEditShortcutViewModel> { parametersOf(args.userId) }

    private val args by navArgs<WalletEditShortcutFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        ItemTouchHelper(WalletEditShortcutItemTouchHelper(this)).attachToRecyclerView(binding.recyclerView)
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.shortcut,
            rightTextButtonAction = {
                if (adapter.items.isEmpty()) {
                    backPress()
                } else {
                    showLoading()
                    viewModel.sortWalletShortcuts(adapter.items)
                }
            },
            rightTextButtonMessage = R.string.save,
        )
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.onItemUpdate(it)
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.onDeleteSuccess.collectLatest {
                dismissLoading()
                adapter.onItemDelete(it)
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                toast(it.message)
            }
        }
        lifecycleScope.launch {
            viewModel.onSaveSuccess.collectLatest {
                dismissLoading()
                backPress()
            }
        }
    }

    override fun onItemDelete(position: Int, shortcut: WalletShortcutEntity) {
        showLoading()
        viewModel.deleteShortcut(position, shortcut.id)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        adapter.onItemMove(fromPosition, toPosition)
    }

    private val adapter by lazy {
        WalletEditShortcutAdapter(compositeDisposable, this)
    }

    private val binding by lazy {
        FragmentWalletEditShortcutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = binding.root

}