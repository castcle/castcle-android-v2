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

package com.castcle.android.presentation.wallet.wallet_dashboard

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.custom_view.load_state.item_error_append.ErrorAppendViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading_append.LoadingAppendViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.DialogBasicBinding
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_balance.WalletDashboardBalanceViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty.WalletDashboardEmptyViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_history.WalletDashboardHistoryViewRenderer
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogFragment.Companion.SELECT_FILTER
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogFragment.Companion.SELECT_USER
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import com.castcle.android.presentation.wallet.wallet_transaction.WalletTransactionFragment.Companion.TRANSACTION_SUCCESS
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletDashboardFragment : BaseFragment(), WalletDashboardListener {

    private val viewModel by viewModel<WalletDashboardViewModel>()

    private val directions = WalletDashboardFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setRefreshColor(
            backgroundColor = R.color.black_background_3,
            iconColor = R.color.blue,
        )
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            rightButtonAction = {
                val userId = viewModel.userId.value.orEmpty()
                val requestType = WalletScanQrCodeRequestType.FromDashboard(userId)
                directions.toWalletScanQrCodeFragment(requestType).navigate()
            },
            rightButtonIcon = R.drawable.ic_qr_code,
            title = R.string.wallet,
        )
    }

    @Suppress("DEPRECATION")
    override fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.refreshData()
        }
        setFragmentResultListener(SELECT_FILTER) { _, bundle ->
            bundle.getParcelable<Parcelable>(SELECT_FILTER)
                ?.cast<WalletHistoryFilter>()
                ?.let { viewModel.filter.value = it }
        }
        setFragmentResultListener(SELECT_USER) { _, bundle ->
            bundle.getString(SELECT_USER)?.let { viewModel.userId.value = it }
        }
        setFragmentResultListener(TRANSACTION_SUCCESS) { _, bundle ->
            if (bundle.getBoolean(TRANSACTION_SUCCESS, false)) {
                viewModel.refreshData()
            }
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            binding.loadStateRefreshView.bind(
                loadState = viewModel.loadState,
                recyclerView = binding.recyclerView,
                type = LoadStateRefreshItemsType.DEFAULT,
            )
        }
    }

    override fun onAirdropClicked() {
        viewModel.getAccessToken {
            openUrl(getString(R.string.airdrop_url, it))
        }
    }

    override fun onDepositClicked(currentUserId: String) {
        directions.toWalletDepositFragment(currentUserId).navigate()
    }

    override fun onFilterClicked(currentFilter: WalletHistoryFilter) {
        directions.toWalletDashboardDialogFragment(currentFilter, null).navigate()
    }

    override fun onInfoClicked(balance: String) {
        BasicDialog(
            binding = DialogBasicBinding.inflate(layoutInflater),
            button = string(R.string.close),
            title = getString(R.string.fragment_wallet_dashboard_title_6, balance),
        ).show()
    }

    override fun onSelectUserClicked(currentUserId: String) {
        directions.toWalletDashboardDialogFragment(null, currentUserId).navigate()
    }

    override fun onSendClicked(currentUserId: String) {
        directions.toWalletSendFragment(
            targetCastcleId = null,
            targetUserId = null,
            userId = currentUserId,
        ).navigate()
    }

    override fun onStart() {
        super.onStart()
        viewModel.trackViewWallet()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorAppendViewRenderer())
            registerRenderer(LoadingAppendViewRenderer())
            registerRenderer(WalletDashboardBalanceViewRenderer())
            registerRenderer(WalletDashboardEmptyViewRenderer())
            registerRenderer(WalletDashboardHistoryViewRenderer())
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