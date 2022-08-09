package com.castcle.android.presentation.feed

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity

interface FeedListener : CastcleListener {
    fun onCommentClicked(cast: CastEntity, user: UserEntity) = Unit
    fun onDeleteImageClicked(index: Int) = Unit
    fun onFollowClicked(user: UserEntity) = Unit
    fun onHashtagClicked(keyword: String) = Unit
    fun onImageClicked(photo: ImageEntity) = Unit
    fun onLikeClicked(cast: CastEntity) = Unit
    fun onLinkClicked(url: String) = Unit
    fun onNewCastClicked(userId: String) = Unit
    fun onContentOptionClicked(cast: CastEntity, user: UserEntity) = Unit
    fun onRecastClicked(cast: CastEntity) = Unit
    fun onUserClicked(user: UserEntity) = Unit
    fun onWhoToFollowClicked() = Unit
}