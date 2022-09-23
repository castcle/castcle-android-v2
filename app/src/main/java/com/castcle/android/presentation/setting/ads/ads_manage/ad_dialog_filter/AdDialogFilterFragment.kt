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

package com.castcle.android.presentation.setting.ads.ads_manage.ad_dialog_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.domain.ads.type.AdFilterType
import com.castcle.android.presentation.setting.ads.ads_manage.ad_dialog_filter.item_ad_filter.AdDialogFilterViewRenderer
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdDialogFilterFragment : BaseBottomSheetDialogFragment(),
    AdDialogFilterListener {

    private val viewModel by viewModel<AdFilterViewModel>()

    private val args by navArgs<AdDialogFilterFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        with(viewModel) {
            getItems(args.adFilter)
            views.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onFilterClicked(filter: AdFilterType) {
        setFragmentResult(SELECT_AD_FILTER, bundleOf(SELECT_AD_FILTER to filter))
        backPress()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(AdDialogFilterViewRenderer())
        }
    }

    private val binding by lazy {
        DialogOptionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    companion object {
        const val SELECT_AD_FILTER = "SELECT_AD_FILTER"
    }

}