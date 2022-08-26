package com.castcle.android.domain.metadata

import com.castcle.android.domain.metadata.entity.ReportSubjectEntity

interface MetadataRepository {
    suspend fun fetchCountryCode()
    suspend fun getReportSubject(): List<ReportSubjectEntity>
}