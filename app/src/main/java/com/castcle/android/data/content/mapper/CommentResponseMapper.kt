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