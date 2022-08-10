package com.castcle.android.presentation.report.report_subject

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.metadata.entity.ReportSubjectEntity

interface ReportSubjectListener : CastcleListener {
    fun onSubjectClicked(subject: ReportSubjectEntity)
    fun onTermsClicked()
}