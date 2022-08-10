package com.castcle.android.domain.metadata.entity

import android.os.Parcelable
import com.castcle.android.data.metadata.entity.ReportSubjectResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportSubjectEntity(
    val name: String = "",
    val slug: String = "",
) : Parcelable {

    companion object {
        fun map(response: List<ReportSubjectResponse>?) = response.orEmpty()
            .sortedBy { it.order }
            .map { ReportSubjectEntity(name = it.name.orEmpty(), slug = it.slug.orEmpty()) }
    }

}