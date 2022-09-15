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

package com.castcle.android.presentation.wallet.wallet_add_shortcut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.string
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.DialogBasicBinding
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.wallet.wallet_add_shortcut.item_wallet_add_shortcut.WalletAddShortcutViewRenderer
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.ADDRESS_RESULT
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_AVATAR
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_CASTCLE_ID
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_USER_ID
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletAddShortcutFragment : BaseFragment(), WalletAddShortcutListener {

    private val viewModel by viewModel<WalletAddShortcutViewModel>()

    private val args by navArgs<WalletAddShortcutFragmentArgs>()

    private val directions = WalletAddShortcutFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isEnabled = false
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_wallet_add_shortcut_title_1,
        )
    }

    override fun initListener() {
        setFragmentResultListener(ADDRESS_RESULT) { _, bundle ->
            val avatar = bundle.getString(RESULT_AVATAR).orEmpty()
            val castcleId = bundle.getString(RESULT_CASTCLE_ID).orEmpty()
            val userId = bundle.getString(RESULT_USER_ID).orEmpty()
            val items = viewModel.views.value?.copy(
                avatar = avatar,
                castcleId = castcleId,
                userId = userId,
            )
            viewModel.views.postValue(items)
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
                BasicDialog(
                    binding = DialogBasicBinding.inflate(layoutInflater),
                    button = string(R.string.close),
                    isCancelable = false,
                    title = string(R.string.fragment_wallet_add_shortcut_title_5),
                ) {
                    backPress()
                }.show()
            }
        }
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                toast(it.message)
            }
        }
    }

    override fun onCastcleIdClicked() {
        directions.toWalletAddressFragment(args.userId).navigate()
    }

    override fun onCreateShortcut(userId: String) {
        showLoading()
        viewModel.createWalletShortcut(userId)
    }

    override fun onScanQrCodeClicked() {
        directions
            .toWalletScanQrCodeFragment(WalletScanQrCodeRequestType.FromAddress(args.userId))
            .navigate()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletAddShortcutViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}