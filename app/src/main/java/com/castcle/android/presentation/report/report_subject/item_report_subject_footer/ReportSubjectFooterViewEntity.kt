package com.castcle.android.presentation.report.report_subject.item_report_subject_footer

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ReportSubjectFooterViewEntity(
    override val uniqueId: String = "${R.layout.item_report_subject_footer}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ReportSubjectFooterViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ReportSubjectFooterViewEntity>() == this
    }

    override fun viewType() = R.layout.item_report_subject_footer

}