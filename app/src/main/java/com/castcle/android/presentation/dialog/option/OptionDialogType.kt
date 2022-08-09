package com.castcle.android.presentation.dialog.option

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class OptionDialogType : Parcelable {

    @Parcelize
    data class DeleteContent(val contentId: String) : OptionDialogType()

    @Parcelize
    data class ReportContent(val contentId: String) : OptionDialogType()

}