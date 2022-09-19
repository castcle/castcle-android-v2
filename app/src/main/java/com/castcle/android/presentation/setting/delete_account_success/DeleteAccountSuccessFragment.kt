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

package com.castcle.android.presentation.setting.delete_account_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.FragmentDeleteAccountSuccessBinding
import com.castcle.android.domain.user.type.UserType
import io.reactivex.rxkotlin.plusAssign

class DeleteAccountSuccessFragment : BaseFragment() {

    private val args by navArgs<DeleteAccountSuccessFragmentArgs>()

    override fun initViewProperties() {
        binding.tvTitle.text = if (args.type is UserType.People) {
            string(R.string.fragment_delete_account_success_title_2)
        } else {
            string(R.string.fragment_delete_account_success_title_3)
        }
    }

    override fun initListener() {
        compositeDisposable += binding.tvConfirm.onClick {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
        addOnBackPressedCallback {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private val binding by lazy {
        FragmentDeleteAccountSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = binding.root

}