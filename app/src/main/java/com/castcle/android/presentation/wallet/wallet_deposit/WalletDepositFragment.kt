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

package com.castcle.android.presentation.wallet.wallet_deposit

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.LoadStateRefreshItemsType
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit.WalletDepositViewRenderer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WalletDepositFragment : BaseFragment(), WalletDepositListener {

    private val viewModel by viewModel<WalletDepositViewModel> { parametersOf(args.userId) }

    private val args by navArgs<WalletDepositFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isEnabled = false
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.fragment_wallet_deposit_title_1,
        )
    }

    override fun initListener() {
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

    override fun onSaveQrCodeWallet(bitmap: Bitmap) {
        activity?.saveImage(bitmap, System.currentTimeMillis().toString())
        toast("Image saved")
    }

    override fun onShareQrCodeWallet(bitmap: Bitmap) {
        activity?.shareImageUri(bitmap.saveImageToCache(requireContext()))
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(WalletDepositViewRenderer())
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