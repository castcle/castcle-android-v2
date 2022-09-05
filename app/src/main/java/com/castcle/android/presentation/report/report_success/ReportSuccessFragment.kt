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

package com.castcle.android.presentation.report.report_success

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.constants.URL_TERMS_OF_SERVICE
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentReportSuccessBinding
import io.reactivex.rxkotlin.plusAssign

class ReportSuccessFragment : BaseFragment() {

    private val args by navArgs<ReportSuccessFragmentArgs>()

    override fun initViewProperties() {
        val textColor = ForegroundColorSpan(color(R.color.blue))
        val text = if (args.isReportContent) {
            SpannableString(string(R.string.fragment_report_success_description_cast))
        } else {
            SpannableString(string(R.string.fragment_report_success_description_user))
        }
        text.setSpan(textColor, 22, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
        binding.tvDescription.text = text
        binding.actionBar.bind(
            leftButtonIcon = null,
            title = if (args.isReportContent) {
                R.string.report_cast_title
            } else {
                R.string.report_user_title
            },
        )
    }

    override fun initListener() {
        compositeDisposable += binding.tvDone.onClick {
            backPress()
        }
        compositeDisposable += binding.tvDescription.onClick {
            openUrl(URL_TERMS_OF_SERVICE)
        }
    }

    private val binding by lazy {
        FragmentReportSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}