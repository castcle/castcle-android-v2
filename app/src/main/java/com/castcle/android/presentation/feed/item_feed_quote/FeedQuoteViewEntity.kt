package com.castcle.android.presentation.feed.item_feed_quote

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity

data class FeedQuoteViewEntity(
    val cast: CastEntity = CastEntity(),
    val feedId: String = "",
    val reference: CastcleViewEntity = FeedTextViewEntity(),
    val referenceCast: CastEntity? = null,
    override val uniqueId: String = "",
    val user: UserEntity = UserEntity(),
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedQuoteViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedQuoteViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_quote

}