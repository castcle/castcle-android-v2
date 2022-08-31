package com.castcle.android.presentation.report.report_subject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.error
import com.castcle.android.core.extensions.loading
import com.castcle.android.domain.metadata.MetadataRepository
import com.castcle.android.presentation.report.report_subject.item_report_subject_footer.ReportSubjectFooterViewEntity
import com.castcle.android.presentation.report.report_subject.item_report_subject_header.ReportSubjectHeaderViewEntity
import com.castcle.android.presentation.report.report_subject.item_report_subject_item.ReportSubjectItemViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReportSubjectViewModel(
    private val isReportContent: Boolean,
    private val repository: MetadataRepository,
    private val state: SavedStateHandle,
) : BaseViewModel() {

    val views = MutableLiveData<List<CastcleViewEntity>>()

    init {
        getReportSubject()
    }

    private fun getReportSubject() {
        launch(
            onError = { views.error(it) { getReportSubject() } },
            onLaunch = { views.loading() },
        ) {
            val headerItem = ReportSubjectHeaderViewEntity(isReportContent = isReportContent)
            val footerItem = ReportSubjectFooterViewEntity()
            val subjectItem = repository.getReportSubject().map {
                ReportSubjectItemViewEntity(
                    subject = it,
                    uniqueId = it.slug,
                )
            }
            views.postValue(listOf(headerItem).plus(subjectItem).plus(footerItem))
        }
    }

    fun saveItemsState(layoutManager: RecyclerView.LayoutManager) {
        state[SAVE_STATE_RECYCLER_VIEW] = layoutManager.onSaveInstanceState()
    }

    fun restoreItemsState(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.onRestoreInstanceState(state[SAVE_STATE_RECYCLER_VIEW])
    }

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }

}