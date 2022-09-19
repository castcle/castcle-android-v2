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

package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_filter_dialog.WalletDashboardDialogFilterViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog.WalletDashboardDialogUserViewRenderer
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletDashboardDialogFragment : BaseBottomSheetDialogFragment(),
    WalletDashboardDialogListener {

    private val viewModel by viewModel<WalletDashboardDialogViewModel>()

    private val args by navArgs<WalletDashboardDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        viewModel.getItems(args.currentFilter, args.currentUserId)
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCancelClicked() {
        backPress()
    }

    override fun onFilterClicked(filter: WalletHistoryFilter) {
        setFragmentResult(SELECT_FILTER, bundleOf(SELECT_FILTER to filter))
        backPress()
    }

    override fun onUserClicked(user: UserEntity) {
        setFragmentResult(SELECT_USER, bundleOf(SELECT_USER to user.id))
        backPress()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletDashboardDialogFilterViewRenderer())
            registerRenderer(WalletDashboardDialogUserViewRenderer())
        }
    }

    private val binding by lazy {
        DialogOptionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    companion object {
        const val SELECT_FILTER = "SELECT_FILTER"
        const val SELECT_USER = "SELECT_USER"
    }

}