package com.castcle.android.presentation.report.report_subject.item_report_subject_header

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ReportSubjectHeaderViewEntity(
    val isReportContent: Boolean = true,
    override val uniqueId: String = "${R.layout.item_report_subject_header}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ReportSubjectHeaderViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ReportSubjectHeaderViewEntity>() == this
    }

    override fun viewType() = R.layout.item_report_subject_header

}