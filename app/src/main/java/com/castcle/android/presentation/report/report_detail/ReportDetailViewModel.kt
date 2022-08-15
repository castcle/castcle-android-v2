package com.castcle.android.presentation.report.report_detail

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.user.entity.ReportRequest
import com.castcle.android.domain.user.UserRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReportDetailViewModel(
    private val database: CastcleDatabase,
    private val isReportContent: Boolean,
    private val repository: UserRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    fun report(contentId: String?, message: String, subject: String, userId: String?) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            val castcleId = userId?.let { database.user().get(it).firstOrNull()?.castcleId }
            val body = ReportRequest(
                message = message,
                subject = subject,
                targetCastcleId = castcleId,
                targetContentId = contentId,
            )
            if (isReportContent) {
                repository.reportContent(body)
            } else {
                repository.reportUser(body)
            }
        }
    }

}