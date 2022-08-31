package com.castcle.android.presentation.content.item_comment

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.user.entity.UserEntity

data class CommentViewEntity(
    val comment: CommentEntity = CommentEntity(),
    val showLine: Boolean = false,
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<CommentViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<CommentViewEntity>() == this
    }

    override fun viewType() = R.layout.item_comment

}