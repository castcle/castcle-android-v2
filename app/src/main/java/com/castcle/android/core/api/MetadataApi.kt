package com.castcle.android.core.api

import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.data.metadata.entity.ReportSubjectResponse
import retrofit2.Response
import retrofit2.http.GET

interface MetadataApi {

    @GET("v2/metadata/report-subjects")
    suspend fun getReportSubject(): Response<BaseResponse<List<ReportSubjectResponse>>>

}