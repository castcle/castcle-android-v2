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

package com.castcle.android.presentation.wallet.wallet_otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.DialogBasicBinding
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.wallet.type.WalletTransactionType
import com.castcle.android.presentation.dialog.basic.BasicDialog
import com.castcle.android.presentation.wallet.wallet_otp.WalletOtpViewModel.WalletOtpViewModelParameter
import com.castcle.android.presentation.wallet.wallet_otp.item_wallet_otp.WalletOtpViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletOtpFragment : BaseFragment(), WalletOtpListener {

    private val viewModel by viewModel<WalletOtpViewModel> {
        parametersOf(WalletOtpViewModelParameter(args.otpEmail, args.otpMobile, args.request))
    }

    private val args by navArgs<WalletOtpFragmentArgs>()

    private val directions = WalletOtpFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isEnabled = false
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_wallet_otp_title_1,
        )
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest {
                dismissLoading()
                adapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            viewModel.onSuccess.collectLatest {
                dismissLoading()
                directions.toWalletTransactionFragment(
                    request = args.request,
                    type = WalletTransactionType.Completed,
                ).navigate(
                    popUpTo = R.id.walletSendFragment,
                )
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

    override fun onConfirmClicked(otpEmail: OtpEntity, otpMobile: OtpEntity) {
        showLoading()
        hideKeyboard()
        viewModel.confirmTransaction(otpEmail, otpMobile)
    }

    override fun onResendOtpEmail(otp: OtpEntity) {
        showLoading()
        viewModel.resendOtpEmail(otp)
    }

    override fun onResendOtpMobile(otp: OtpEntity) {
        showLoading()
        viewModel.resendOtpMobile(otp)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletOtpViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

}