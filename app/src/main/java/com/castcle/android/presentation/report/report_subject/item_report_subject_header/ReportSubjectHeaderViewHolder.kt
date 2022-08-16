package com.castcle.android.presentation.report.report_subject.item_report_subject_header

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.databinding.ItemReportSubjectHeaderBinding

class ReportSubjectHeaderViewHolder(
    private val binding: ItemReportSubjectHeaderBinding,
) : CastcleViewHolder<ReportSubjectHeaderViewEntity>(binding.root) {

    override var item = ReportSubjectHeaderViewEntity()

    override fun bind(bindItem: ReportSubjectHeaderViewEntity) {
        binding.tvTitle.text = itemView.context.getString(
            if (item.isReportContent) {
                R.string.fragment_report_subject_header_cast
            } else {
                R.string.fragment_report_subject_header_user
            }
        )
    }

}