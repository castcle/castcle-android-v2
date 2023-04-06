/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.presentation.dialog.option

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed class OptionDialogType : Parcelable {

    @Parcelize
    data class MyCommentOption(val castcleId: String, val commentId: String) : OptionDialogType() {
        @IgnoredOnParcel val deleteComment = 0
        @IgnoredOnParcel val replyComment = 1
    }

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
    data class NotificationOption(val notificationId: String, val isRead: Boolean) : OptionDialogType() {
        @IgnoredOnParcel val remove = 0
        @IgnoredOnParcel val markAsRead = 1
    }

    @Parcelize
    data class OtherCommentOption(val castcleId: String, val commentId: String) :
        OptionDialogType() {
        @IgnoredOnParcel val replyComment = 0
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

    @Parcelize
    data class ReplyOption(val replyCommentId: String) : OptionDialogType() {
        @IgnoredOnParcel val deleteReply = 0
    }

    @Parcelize
    object CameraOption : OptionDialogType() {
        @IgnoredOnParcel val selectCamera = 0
        @IgnoredOnParcel val selectGallery = 1
    }

}