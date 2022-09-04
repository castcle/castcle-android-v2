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

package com.castcle.android.presentation.setting.update_profile_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.authentication.type.OtpObjective
import com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success.UpdateProfileSuccessViewEntity
import com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success.UpdateProfileSuccessViewRenderer

class UpdateProfileSuccessFragment : BaseFragment(), UpdateProfileSuccessListener {

    private val args by navArgs<UpdateProfileSuccessFragmentArgs>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.actionBar.bind(
            leftButtonIcon = null,
            title = when (args.otp.objective) {
                is OtpObjective.ChangePassword,
                is OtpObjective.ForgotPassword -> R.string.password
                is OtpObjective.VerifyMobile -> R.string.mobile_number
            },
        )
        adapter.submitList(listOf(getItems()))
    }

    private fun getItems() = UpdateProfileSuccessViewEntity(
        close = when (args.otp.objective) {
            is OtpObjective.ForgotPassword -> string(R.string.fragment_update_profile_success_title_5)
            else -> string(R.string.close)
        },
        description = when (args.otp.objective) {
            is OtpObjective.ChangePassword,
            is OtpObjective.ForgotPassword -> string(R.string.fragment_update_profile_success_title_4)
            is OtpObjective.VerifyMobile -> string(R.string.fragment_update_profile_success_title_2)
        },
        icon = R.drawable.ic_update_profile_success,
        title = when (args.otp.objective) {
            is OtpObjective.ChangePassword,
            is OtpObjective.ForgotPassword -> string(R.string.fragment_update_profile_success_title_3)
            is OtpObjective.VerifyMobile -> string(R.string.fragment_update_profile_success_title_1)
        },
    )

    override fun onCloseClicked() {
        backPress()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(UpdateProfileSuccessViewRenderer())
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