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

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OptionDialogViewModel(
    private val contentRepository: ContentRepository,
    private val database: CastcleDatabase,
    private val type: OptionDialogType,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    val views = MutableLiveData<List<CastcleViewEntity>>()

    fun deleteComment(commentId: String) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            userRepository.deleteComment(commentId = commentId)
        }
    }

    fun deleteContent(contentId: String) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            val userId = database.cast().get(castId = contentId)?.user?.id.orEmpty()
            database.withTransaction {
                contentRepository.deleteContent(contentId = contentId, userId = userId)
                userRepository.getUser(id = userId)
            }
        }
    }

    fun deleteReply(replyCommentId: String) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            userRepository.deleteReplyComment(replyCommentId = replyCommentId)
        }
    }

    fun getOptionItems(context: Context) = launch {
        val items = when (type) {
            is OptionDialogType.MyCommentOption -> {
                val replyContentItem = OptionDialogViewEntity(
                    eventType = type.replyComment,
                    icon = R.drawable.ic_rerply,
                    title = context.getString(R.string.reply)
                )
                val deleteContentItem = OptionDialogViewEntity(
                    eventType = type.deleteComment,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete)
                )
                listOf(replyContentItem, deleteContentItem)
            }
            is OptionDialogType.MyContentOption -> {
                val deleteContentItem = OptionDialogViewEntity(
                    eventType = type.deleteContent,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete)
                )
                listOf(deleteContentItem)
            }
            is OptionDialogType.MyPageOption -> {
                val deletePageItem = OptionDialogViewEntity(
                    eventType = type.deletePage,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete_page)
                )
                val syncSocialMediaItem = OptionDialogViewEntity(
                    eventType = type.syncSocialMedia,
                    icon = R.drawable.ic_sync_social_media,
                    title = context.getString(R.string.sync_social_media)
                )
                listOf(deletePageItem, syncSocialMediaItem)
            }
            is OptionDialogType.MyUserOption -> {
                val syncSocialMediaItem = OptionDialogViewEntity(
                    eventType = type.syncSocialMedia,
                    icon = R.drawable.ic_sync_social_media,
                    title = context.getString(R.string.sync_social_media)
                )
                listOf(syncSocialMediaItem)
            }
            is OptionDialogType.OtherCommentOption -> {
                val replyContentItem = OptionDialogViewEntity(
                    eventType = type.replyComment,
                    icon = R.drawable.ic_rerply,
                    title = context.getString(R.string.reply)
                )
                listOf(replyContentItem)
            }
            is OptionDialogType.OtherContentOption -> {
                val reportContentItem = OptionDialogViewEntity(
                    eventType = type.reportContent,
                    icon = R.drawable.ic_report,
                    title = context.getString(R.string.report_cast)
                )
                listOf(reportContentItem)
            }
            is OptionDialogType.OtherUserOption -> {
                val castcleId = database.user().get(type.userId).firstOrNull()?.castcleId.orEmpty()
                val blockUserItem = OptionDialogViewEntity(
                    eventType = type.blockUser,
                    icon = R.drawable.ic_block,
                    title = context.getString(R.string.block_user, castcleId)
                )
                val reportUserItem = OptionDialogViewEntity(
                    eventType = type.reportUser,
                    icon = R.drawable.ic_report,
                    title = context.getString(R.string.report_user, castcleId)
                )
                listOf(blockUserItem, reportUserItem)
            }
            is OptionDialogType.ReplyOption -> {
                val deleteReplyItem = OptionDialogViewEntity(
                    eventType = type.deleteReply,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete)
                )
                listOf(deleteReplyItem)
            }
        }
        views.postValue(items)
    }

}