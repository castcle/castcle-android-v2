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