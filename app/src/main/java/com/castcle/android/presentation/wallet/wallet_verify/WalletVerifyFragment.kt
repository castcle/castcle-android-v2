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

package com.castcle.android.presentation.wallet.wallet_verify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.presentation.setting.account.AccountListener
import com.castcle.android.presentation.setting.account.item_menu.AccountMenuViewRenderer
import com.castcle.android.presentation.setting.account.item_title.AccountTitleViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletVerifyFragment : BaseFragment(), AccountListener {

    private val viewModel by viewModel<WalletVerifyViewModel>()

    private val directions = WalletVerifyFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = R.string.account_setting,
        )
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest(adapter::submitList)
        }
    }

    override fun onRegisterEmailClicked() {
        directions.toRegisterEmailFragment().navigate(R.id.walletVerifyFragment)
    }

    override fun onRequestOtpClicked(type: OtpType) {
        directions.toRequestOtpFragment(type).navigate(R.id.walletVerifyFragment)
    }

    override fun onResentVerifyEmailClicked() {
        directions.toResentVerifyEmailFragment().navigate(R.id.walletVerifyFragment)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(AccountMenuViewRenderer())
            registerRenderer(AccountTitleViewRenderer())
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