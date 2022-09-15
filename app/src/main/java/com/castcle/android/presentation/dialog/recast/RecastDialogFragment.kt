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

package com.castcle.android.presentation.dialog.recast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.presentation.dialog.option.OptionDialogListener
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewRenderer
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewRenderer
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewRenderer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class RecastDialogFragment : BaseBottomSheetDialogFragment(), OptionDialogListener,
    RecastDialogListener {

    private val viewModel by stateViewModel<RecastDialogViewModel> { parametersOf(args.contentId) }

    private val args by navArgs<RecastDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutParams
            ?.cast<ConstraintLayout.LayoutParams>()
            ?.height = dimenPx(com.intuit.sdp.R.dimen._162sdp)
    }

    override fun initObserver() {
        viewModel.recastFail.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.recastSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
    }

    @FlowPreview
    override fun initConsumer() {
        lifecycleScope.launch {
            viewModel.views.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    override fun onOptionClicked(eventType: Int) {
        when (eventType) {
            R.string.quote_cast -> navigateToCreateQuoteCast()
            R.string.recast, R.string.unrecast -> {
                showLoading()
                viewModel.recast(isRecasted = eventType == R.string.unrecast)
            }
        }
    }

    override fun onSelectUserClicked() {
        viewModel.showSelectUser()
    }

    override fun onUserClicked(userId: String) {
        viewModel.updateSelectedUser(userId)
    }

    private fun navigateToCreateQuoteCast() {
        val currentUserId = viewModel.currentUser.value?.find { it.first }?.second?.id.orEmpty()
        RecastDialogFragmentDirections
            .toNewCastFragment(args.contentId, currentUserId)
            .navigate()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(OptionDialogViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(RecastTitleViewRenderer())
            registerRenderer(SelectRecastUserViewRenderer())
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

}