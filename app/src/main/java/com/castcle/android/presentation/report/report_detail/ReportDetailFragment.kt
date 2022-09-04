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

package com.castcle.android.presentation.report.report_detail

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentReportDetailBinding
import io.reactivex.rxkotlin.plusAssign
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReportDetailFragment : BaseFragment() {

    private val viewModel by viewModel<ReportDetailViewModel> {
        parametersOf(args.contentId != null)
    }

    private val args by navArgs<ReportDetailFragmentArgs>()

    override fun initViewProperties() {
        updateSubmitButton()
        binding.groupMessage.isVisible = enableMessage()
        binding.tvSubject.text = when {
            enableMessage() && args.contentId != null -> string(R.string.fragment_report_detail_cast_title)
            enableMessage() && args.userId != null -> string(R.string.fragment_report_detail_user_title)
            else -> args.subject.name
        }
        binding.actionBar.bind(
            leftButtonAction = { backPress() },
            title = if (args.contentId != null) {
                R.string.report_cast_title
            } else {
                R.string.report_user_title
            },
        )
    }

    override fun initObserver() {
        viewModel.onError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
        viewModel.onSuccess.observe(viewLifecycleOwner) {
            dismissLoading()
            ReportDetailFragmentDirections
                .toReportSuccessFragment(isReportContent = args.contentId != null)
                .navigate()
        }
    }

    override fun initListener() {
        compositeDisposable += binding.etMessage.onTextChange {
            updateSubmitButton()
        }
        compositeDisposable += binding.tvSubmit.onClick {
            showLoading()
            hideKeyboard()
            viewModel.report(
                contentId = args.contentId,
                message = binding.etMessage.text.toString().trim(),
                subject = args.subject.slug,
                userId = args.userId,
            )
        }
    }

    private fun updateSubmitButton() {
        val text = binding.etMessage.text.toString().trim()
        val isEnable = !enableMessage() || text.isNotBlank()
        binding.tvSubmit.isEnabled = isEnable
        binding.tvSubmit.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

    private fun enableMessage() = args.subject.slug == "something-else"

    private val binding by lazy {
        FragmentReportDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}