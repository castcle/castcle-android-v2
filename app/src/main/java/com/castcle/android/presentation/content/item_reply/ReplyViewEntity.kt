package com.castcle.android.presentation.content.item_reply

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.UserEntity

data class ReplyViewEntity(
    val comment: CommentEntity = CommentEntity(),
    val showLine: Boolean = false,
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ReplyViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ReplyViewEntity>() == this
    }

    override fun viewType() = R.layout.item_reply

}