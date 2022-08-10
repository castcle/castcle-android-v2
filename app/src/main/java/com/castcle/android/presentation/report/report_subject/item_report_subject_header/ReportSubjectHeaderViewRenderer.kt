package com.castcle.android.presentation.report.report_subject.item_report_subject_header

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemReportSubjectHeaderBinding
import com.castcle.android.presentation.report.report_subject.ReportSubjectListener
import io.reactivex.disposables.CompositeDisposable

class ReportSubjectHeaderViewRenderer : CastcleViewRenderer<ReportSubjectHeaderViewEntity,
    ReportSubjectHeaderViewHolder,
    ReportSubjectListener>(R.layout.item_report_subject_header) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ReportSubjectListener,
        compositeDisposable: CompositeDisposable
    ) = ReportSubjectHeaderViewHolder(
        ItemReportSubjectHeaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}