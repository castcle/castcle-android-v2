package com.castcle.android.data.metadata

import com.castcle.android.core.api.MetadataApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.domain.metadata.MetadataRepository
import com.castcle.android.domain.metadata.entity.ReportSubjectEntity
import org.koin.core.annotation.Factory

@Factory
class MetadataRepositoryImpl(
    private val api: MetadataApi,
) : MetadataRepository {

    override suspend fun getReportSubject(): List<ReportSubjectEntity> {
        val response = apiCall { api.getReportSubject() }
        return ReportSubjectEntity.map(response?.payload)
    }

}