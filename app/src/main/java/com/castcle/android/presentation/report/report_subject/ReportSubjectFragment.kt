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

package com.castcle.android.presentation.report.report_subject

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.constants.URL_TERMS_OF_SERVICE
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewRenderer
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewRenderer
import com.castcle.android.core.extensions.openUrl
import com.castcle.android.databinding.LayoutRecyclerViewBinding
import com.castcle.android.domain.metadata.entity.ReportSubjectEntity
import com.castcle.android.presentation.report.report_subject.item_report_subject_footer.ReportSubjectFooterViewRenderer
import com.castcle.android.presentation.report.report_subject.item_report_subject_header.ReportSubjectHeaderViewRenderer
import com.castcle.android.presentation.report.report_subject.item_report_subject_item.ReportSubjectItemViewRenderer
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class ReportSubjectFragment : BaseFragment(), ReportSubjectListener {

    private val viewModel by stateViewModel<ReportSubjectViewModel> {
        parametersOf(args.contentId != null)
    }

    private val args by navArgs<ReportSubjectFragmentArgs>()

    override fun initViewProperties() {
        binding.swipeRefresh.isEnabled = false
        binding.recyclerView.adapter = adapter
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
        viewModel.views.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onSubjectClicked(subject: ReportSubjectEntity) {
        ReportSubjectFragmentDirections
            .toReportDetailFragment(args.contentId, subject, args.userId)
            .navigate()
    }

    override fun onTermsClicked() {
        openUrl(URL_TERMS_OF_SERVICE)
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.layoutManager?.also(viewModel::restoreItemsState)
    }

    override fun onStop() {
        binding.recyclerView.layoutManager?.also(viewModel::saveItemsState)
        super.onStop()
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(ErrorStateViewRenderer())
            registerRenderer(LoadingViewRenderer())
            registerRenderer(ReportSubjectFooterViewRenderer())
            registerRenderer(ReportSubjectHeaderViewRenderer())
            registerRenderer(ReportSubjectItemViewRenderer())
        }
    }

    private val binding by lazy {
        LayoutRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}