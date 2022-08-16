package com.castcle.android.presentation.dialog.option

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed class OptionDialogType : Parcelable {

    @Parcelize
    data class MyContentOption(val contentId: String) : OptionDialogType() {
        @IgnoredOnParcel val deleteContent = 0
    }

    @Parcelize
    data class MyPageOption(val userId: String) : OptionDialogType() {
        @IgnoredOnParcel val deletePage = 0
        @IgnoredOnParcel val syncSocialMedia = 1
    }

    @Parcelize
    data class MyUserOption(val userId: String) : OptionDialogType() {
        @IgnoredOnParcel val syncSocialMedia = 0
    }

    @Parcelize
    data class OtherContentOption(val contentId: String) : OptionDialogType() {
        @IgnoredOnParcel val reportContent = 0
    }

    @Parcelize
    data class OtherUserOption(val userId: String) : OptionDialogType() {
        @IgnoredOnParcel val blockUser = 0
        @IgnoredOnParcel val reportUser = 1
    }

}