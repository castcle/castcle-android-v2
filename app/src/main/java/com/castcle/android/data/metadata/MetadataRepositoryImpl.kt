package com.castcle.android.data.metadata

import com.castcle.android.core.api.MetadataApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.domain.metadata.MetadataRepository
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import com.castcle.android.domain.metadata.entity.ReportSubjectEntity
import org.koin.core.annotation.Factory

@Factory
class MetadataRepositoryImpl(
    private val api: MetadataApi,
    private val database: CastcleDatabase,
) : MetadataRepository {

    override suspend fun fetchCountryCode() {
        val response = apiCall { api.getCountryCode() }
        database.countryCode().insert(CountryCodeEntity.map(response?.payload))
    }

    override suspend fun getReportSubject(): List<ReportSubjectEntity> {
        val response = apiCall { api.getReportSubject() }
        return ReportSubjectEntity.map(response?.payload)
    }

}