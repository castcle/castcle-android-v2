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

package com.castcle.android.presentation.wallet.wallet_send

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentWalletSendBinding
import com.castcle.android.domain.wallet.type.WalletTransactionType
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.ADDRESS_RESULT
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.MEMO_RESULT
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_CASTCLE_ID
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_MEMO
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment.Companion.RESULT_USER_ID
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewRenderer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("SetTextI18n")
class WalletSendFragment : BaseFragment(), WalletSendListener {

    private val viewModel by viewModel<WalletSendViewModel> { parametersOf(parameter) }

    private val args by navArgs<WalletSendFragmentArgs>()

    private val directions = WalletSendFragmentDirections

    private val parameter by lazy {
        WalletSendViewModel.WalletSendViewModelParameter(
            targetCastcleId = args.targetCastcleId,
            targetUserId = args.targetUserId,
            userId = args.userId,
        )
    }

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isEnabled = false
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.send,
        )
    }

    override fun initListener() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        compositeDisposable += binding.tvSend.onClick {
            showLoading()
            hideKeyboard()
            viewModel.reviewTransaction()
        }
        setFragmentResultListener(ADDRESS_RESULT) { _, bundle ->
            val castcleId = bundle.getString(RESULT_CASTCLE_ID).orEmpty()
            val userId = bundle.getString(RESULT_USER_ID).orEmpty()
            val items = viewModel.views.value?.copy(
                targetCastcleId = castcleId,
                targetUserId = userId,
            )
            viewModel.views.postValue(items)
        }
        setFragmentResultListener(MEMO_RESULT) { _, bundle ->
            val memo = bundle.getString(RESULT_MEMO).orEmpty()
            val items = viewModel.views.value?.copy(memo = memo)
            viewModel.views.postValue(items)
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
        lifecycleScope.launch {
            viewModel.onError.collectLatest {
                dismissLoading()
                toast(it.message)
            }
        }
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
                directions
                    .toWalletTransactionFragment(request = it, type = WalletTransactionType.Review)
                    .navigate()
            }
        }
    }

    override fun onAddShortcutClicked() = Unit

    override fun onManageShortcutClicked() {
        directions.toWalletShortcutFragment(args.userId).navigate()
    }

    override fun onScanQrCodeClicked(requestType: WalletScanQrCodeRequestType) {
        directions.toWalletScanQrCodeFragment(requestType).navigate()
    }

    override fun onSendToClicked() {
        directions.toWalletAddressFragment(userId = args.userId).navigate()
    }

    override fun onUpdateSendButton(amount: Double, enabled: Boolean) {
        binding.tvAmount.text = amount.asCastToken()
        binding.tvSend.isEnabled = enabled
        binding.tvSend.backgroundTintList = if (enabled) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

    override fun onStop() {
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        changeSoftInputMode(true)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletSendViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentWalletSendBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}