package com.castcle.android.presentation.content

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.UserEntity

interface ContentListener : CastcleListener {
    fun onLikeCommentClicked(comment: CommentEntity)
    fun onLikeCountClicked(contentId: String, hasRecast: Boolean)
    fun onQuoteCastCountClicked(contentId: String)
    fun onRecastCountClicked(contentId: String, hasLike: Boolean)
    fun onReplyClicked(castcleId: String, commentId: String)
    fun onUserClicked(user: UserEntity)
}