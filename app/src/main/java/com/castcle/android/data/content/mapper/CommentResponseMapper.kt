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

package com.castcle.android.data.content.mapper

import androidx.paging.LoadType
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.data.content.entity.CommentResponse
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.user.entity.UserEntity
import org.koin.core.annotation.Factory

@Factory
class CommentResponseMapper {

    data class CommentResponseResult(
        val comment: List<CommentEntity>,
        val content: List<ContentEntity>,
        val user: List<UserEntity>,
    )

    fun apply(
        content: ContentEntity,
        loadType: LoadType,
        ownerUserId: List<String>,
        response: BaseResponse<List<CommentResponse>>?,
        sessionId: Long,
    ): CommentResponseResult {
        val replyItem = CommentEntity.map(ownerUserId, response?.includes?.comments)
        val userItems = UserEntity.map(ownerUserId, response?.includes?.users)
        val commentItem = CommentEntity.map(ownerUserId, response?.payload)
        val headerItems = if (loadType == LoadType.REFRESH) {
            listOf(content, content.toMetricsItem())
        } else {
            listOf()
        }
        val contentItems = commentItem.map { each ->
            val comment = ContentEntity(
                commentId = each.id,
                commentUserId = each.authorId,
                createdAt = each.createdAt,
                sessionId = sessionId,
                type = ContentType.Comment,
            )
            val reply = response?.payload?.find { find -> find.id == each.id }
                ?.reply
                ?.mapNotNull { replyId -> replyItem.find { find -> find.id == replyId } }
                ?.map { reply ->
                    ContentEntity(
                        commentId = reply.id,
                        commentUserId = reply.authorId,
                        createdAt = comment.createdAt,
                        replyAt = reply.createdAt,
                        sessionId = sessionId,
                        type = ContentType.Reply,
                    )
                }
            listOf(comment).plus(reply.orEmpty())
        }
        return CommentResponseResult(
            comment = commentItem.plus(replyItem),
            content = headerItems.plus(contentItems.flatten()),
            user = userItems.distinctBy { it.id },
        )
    }

}