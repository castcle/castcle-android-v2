package com.castcle.android.presentation.report.report_subject.item_report_subject_footer

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemReportSubjectFooterBinding
import com.castcle.android.presentation.report.report_subject.ReportSubjectListener
import io.reactivex.disposables.CompositeDisposable

class ReportSubjectFooterViewRenderer : CastcleViewRenderer<ReportSubjectFooterViewEntity,
    ReportSubjectFooterViewHolder,
    ReportSubjectListener>(R.layout.item_report_subject_footer) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ReportSubjectListener,
        compositeDisposable: CompositeDisposable
    ) = ReportSubjectFooterViewHolder(
        ItemReportSubjectFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}