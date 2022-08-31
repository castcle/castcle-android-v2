package com.castcle.android.presentation.report.report_subject.item_report_subject_item

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemReportSubjectItemBinding
import com.castcle.android.presentation.report.report_subject.ReportSubjectListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ReportSubjectItemViewHolder(
    private val binding: ItemReportSubjectItemBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ReportSubjectListener,
) : CastcleViewHolder<ReportSubjectItemViewEntity>(binding.root) {

    override var item = ReportSubjectItemViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onSubjectClicked(item.subject)
        }
    }

    override fun bind(bindItem: ReportSubjectItemViewEntity) {
        binding.tvName.text = item.subject.name
    }

}