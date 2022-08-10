package com.castcle.android.presentation.report.report_subject.item_report_subject_item

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.metadata.entity.ReportSubjectEntity

data class ReportSubjectItemViewEntity(
    val subject: ReportSubjectEntity = ReportSubjectEntity(),
    override val uniqueId: String = "${R.layout.item_report_subject_item}"
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ReportSubjectItemViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ReportSubjectItemViewEntity>() == this
    }

    override fun viewType() = R.layout.item_report_subject_item

}