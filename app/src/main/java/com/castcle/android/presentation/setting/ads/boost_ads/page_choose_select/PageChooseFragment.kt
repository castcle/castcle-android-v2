package com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.gone
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.LayoutRecyclerStaticViewBinding
import com.castcle.android.domain.ads.type.*
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_choose_header.HeaderViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_object_boost.ChooseObjectiveViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_page_boost.ChoosePageViewRenderer
import com.castcle.android.presentation.setting.ads.boost_ads.page_choose_select.item_payment.PaymentViewRenderer
import org.koin.androidx.viewmodel.ext.android.viewModel

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 19/9/2022 AD at 09:57.

class PageChooseFragment : BaseFragment(), PageChooseListener {

    private val viewModel by viewModel<PageChooseViewModel>()

    private val args by navArgs<PageChooseFragmentArgs>()

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(HeaderViewRenderer())
            registerRenderer(ChooseObjectiveViewRenderer())
            registerRenderer(ChoosePageViewRenderer())
            registerRenderer(PaymentViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerStaticViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun initConsumer() {
        viewModel.fetchItemView(args.boostBundle)
    }

    override fun initViewProperties() {
        binding.recyclerView.adapter = adapter
        binding.btBoostPage.gone()
        binding.clToolBarCast.gone()

        val titleMessage = when (args.boostBundle) {
            is BoostAdBundle.BoostChoosePageBundle -> {
                string(R.string.ad_tool_select_page)
            }
            is BoostAdBundle.BoostChooseObjectiveBundle -> {
                string(R.string.ad_tool_select_page)
            }
            is BoostAdBundle.BoostChoosePaymentBundle -> {
                string(R.string.ad_tool_select_page)
            }
            else -> string(R.string.ad_tool_select_page)
        }
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = titleMessage,
        )
    }

    override fun initObserver() {
        viewModel.itemView.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onChoosePageClick(userId: String) {
        handlerResultBack(SELECT_CHOOSE_PAGE, userId)
    }

    override fun onChooseObjective(objective: ObjectiveType) {
        handlerResultBack(SELECT_CHOOSE_OBJECTIVE, objective)
    }

    override fun onChoosePayment(payment: PaymentType) {
        handlerResultBack(SELECT_CHOOSE_PAYMENT, payment)
    }

    private fun <T> handlerResultBack(idBundle: String, result: T) {
        setFragmentResult(
            idBundle,
            bundleOf(idBundle to result)
        )
        backPress()
    }

    companion object {
        const val SELECT_CHOOSE_PAGE = "choose-page"
        const val SELECT_CHOOSE_OBJECTIVE = "choose-objective"
        const val SELECT_CHOOSE_PAYMENT = "choose-payment"
    }
}