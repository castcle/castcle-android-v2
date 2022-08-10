package com.castcle.android.presentation.report.report_subject.item_report_subject_item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemReportSubjectItemBinding
import com.castcle.android.presentation.report.report_subject.ReportSubjectListener
import io.reactivex.disposables.CompositeDisposable

class ReportSubjectItemViewRenderer : CastcleViewRenderer<ReportSubjectItemViewEntity,
    ReportSubjectItemViewHolder,
    ReportSubjectListener>(R.layout.item_report_subject_item) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ReportSubjectListener,
        compositeDisposable: CompositeDisposable
    ) = ReportSubjectItemViewHolder(
        ItemReportSubjectItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}