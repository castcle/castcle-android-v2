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

package com.castcle.android.presentation.dialog.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.castcle.android.core.base.fragment.BaseBottomSheetDialogFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.toast
import com.castcle.android.databinding.DialogOptionBinding
import com.castcle.android.presentation.content.ContentFragment.Companion.REPLY_RESULT
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewRenderer
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewRenderer
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewRenderer
import com.castcle.android.presentation.sign_up.update_profile.UpdateProfileFragment.Companion.OPTIONAL_SELECT
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class OptionDialogFragment : BaseBottomSheetDialogFragment(), OptionDialogListener {

    private val viewModel by stateViewModel<OptionDialogViewModel> { parametersOf(args.type) }

    private val args by navArgs<OptionDialogFragmentArgs>()

    override fun initViewProperties() {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun initObserver() {
        viewModel.getOptionItems(requireContext())
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            backPress()
        }
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onOptionClicked(eventType: Int) {
        when (val type = args.type) {
            is OptionDialogType.MyCommentOption -> when (eventType) {
                type.deleteComment -> {
                    showLoading()
                    viewModel.deleteComment(type.commentId)
                }
                type.replyComment -> {
                    setFragmentResult(REPLY_RESULT, bundleOf(REPLY_RESULT to type))
                    backPress()
                }
            }
            is OptionDialogType.MyContentOption -> {
                showLoading()
                viewModel.deleteContent(type.contentId)
            }
            is OptionDialogType.MyPageOption -> when (eventType) {
                type.deletePage -> {
                    OptionDialogFragmentDirections
                        .toConfirmDeleteAccountFragment(userId = type.userId)
                        .navigate()
                }
                type.syncSocialMedia -> {
                    OptionDialogFragmentDirections
                        .toSyncSocialFragment(userId = type.userId)
                        .navigate()
                }
            }
            is OptionDialogType.MyUserOption -> {
                OptionDialogFragmentDirections
                    .toSyncSocialFragment(userId = type.userId)
                    .navigate()
            }
            is OptionDialogType.OtherCommentOption -> {
                setFragmentResult(REPLY_RESULT, bundleOf(REPLY_RESULT to type))
                backPress()
            }
            is OptionDialogType.OtherContentOption -> {
                OptionDialogFragmentDirections
                    .toReportSubjectFragment(contentId = type.contentId, userId = null)
                    .navigate()
            }
            is OptionDialogType.OtherUserOption -> when (eventType) {
                type.blockUser -> Unit
                type.reportUser -> {
                    OptionDialogFragmentDirections
                        .toReportSubjectFragment(contentId = null, userId = type.userId)
                        .navigate()
                }
            }
            is OptionDialogType.ReplyOption -> {
                showLoading()
                viewModel.deleteReply(type.replyCommentId)
            }
            is OptionDialogType.CameraOption -> when (eventType) {
                type.selectGallery -> {
                    setFragmentResult(
                        OPTIONAL_SELECT,
                        bundleOf(OPTIONAL_SELECT to type.selectGallery)
                    )
                    backPress()
                }
                type.selectCamera -> {
                    setFragmentResult(
                        OPTIONAL_SELECT,
                        bundleOf(OPTIONAL_SELECT to type.selectCamera)
                    )
                    backPress()
                }
            }
        }
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(OptionDialogViewRenderer())
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