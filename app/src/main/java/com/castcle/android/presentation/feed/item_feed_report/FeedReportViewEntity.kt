package com.castcle.android.presentation.feed.item_feed_report

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class FeedReportViewEntity(
    val ignoreReportContentId: List<String> = listOf(),
    override val uniqueId: String = "",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedReportViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedReportViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_report

}