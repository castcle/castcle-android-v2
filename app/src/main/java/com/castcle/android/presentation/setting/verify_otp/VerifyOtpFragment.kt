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

package com.castcle.android.presentation.setting.verify_otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.domain.authentication.type.OtpType
import com.castcle.android.presentation.setting.verify_otp.item_verify_otp.VerifyOtpViewRenderer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VerifyOtpFragment : BaseFragment(), VerifyOtpListener {

    private val viewModel by viewModel<VerifyOtpViewModel> { parametersOf(args.otp) }

    private val args by navArgs<VerifyOtpFragmentArgs>()

    private val directions = VerifyOtpFragmentDirections

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = when (args.otp.type) {
                is OtpType.Email, is OtpType.Password -> R.string.password
                is OtpType.Mobile -> R.string.mobile_number
            },
        )
    }

    override fun initObserver() {
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.onResentOtpSuccess.collectLatest {
                dismissLoading()
            }
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
                when (it.objective) {
                    is OtpObjective.ChangePassword -> {
                        directions
                            .toChangePasswordFragment(it)
                            .navigate(R.id.requestOtpFragment)
                    }
                    is OtpObjective.ForgotPassword -> {
                        directions
                            .toChangePasswordFragment(it)
                            .navigate(R.id.forgotPasswordFragment)
                    }
                    is OtpObjective.VerifyMobile -> {
                        directions
                            .toUpdateProfileSuccessFragment(it)
                            .navigate(R.id.requestOtpFragment)
                    }
                }
            }
        }
    }

    override fun onResentOtpClicked(otp: OtpEntity) {
        showLoading()
        hideKeyboard()
        viewModel.requestOtp(otp)
    }

    override fun onVerifyOtp(otp: OtpEntity) {
        showLoading()
        hideKeyboard()
        viewModel.verifyOtp(otp)
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
            registerRenderer(VerifyOtpViewRenderer())
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