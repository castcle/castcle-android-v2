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

package com.castcle.android.presentation.wallet.wallet_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentWalletAddressBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.wallet.wallet_address.item_empty_wallet_address.EmptyWalletAddressViewRenderer
import com.castcle.android.presentation.wallet.wallet_address.item_recent_wallet_address.RecentWalletAddressViewRenderer
import com.castcle.android.presentation.wallet.wallet_address.item_wallet_address.WalletAddressViewRenderer
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeFragment
import com.castcle.android.presentation.wallet.wallet_scan_qr_code.WalletScanQrCodeRequestType
import io.reactivex.rxkotlin.plusAssign
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletAddressFragment : BaseFragment(), WalletAddressListener {

    private val viewModel by viewModel<WalletAddressViewModel> { parametersOf(args.userId) }

    private val args by navArgs<WalletAddressFragmentArgs>()

    private val directions = WalletAddressFragmentDirections

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initListener() {
        compositeDisposable += binding.ivLeftIcon.onClick {
            backPress()
        }
        compositeDisposable += binding.ivRightIcon.onClick {
            directions
                .toWalletScanQrCodeFragment(WalletScanQrCodeRequestType.FromAddress(args.userId))
                .navigate(popUpTo = R.id.walletAddressFragment)
        }
        compositeDisposable += binding.etSearch.onTextChange(500) {
            viewModel.updateKeyword(it.trim())
        }
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onUserClicked(user: UserEntity) {
        val bundle = bundleOf(
            WalletScanQrCodeFragment.RESULT_AVATAR to user.avatar.thumbnail,
            WalletScanQrCodeFragment.RESULT_CASTCLE_ID to user.castcleId,
            WalletScanQrCodeFragment.RESULT_USER_ID to user.id,
        )
        setFragmentResult(WalletScanQrCodeFragment.ADDRESS_RESULT, bundle)
        backPress()
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
            registerRenderer(EmptyWalletAddressViewRenderer())
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(RecentWalletAddressViewRenderer())
            registerRenderer(WalletAddressViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentWalletAddressBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}