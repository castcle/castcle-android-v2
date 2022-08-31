package com.castcle.android.presentation.report.report_subject.item_report_subject_footer

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemReportSubjectFooterBinding
import com.castcle.android.presentation.report.report_subject.ReportSubjectListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ReportSubjectFooterViewHolder(
    binding: ItemReportSubjectFooterBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ReportSubjectListener,
) : CastcleViewHolder<ReportSubjectFooterViewEntity>(binding.root) {

    override var item = ReportSubjectFooterViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onTermsClicked()
        }
    }

}