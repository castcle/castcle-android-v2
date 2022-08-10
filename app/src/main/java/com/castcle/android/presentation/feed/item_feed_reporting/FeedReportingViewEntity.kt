package com.castcle.android.presentation.feed.item_feed_reporting

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class FeedReportingViewEntity(
    val reportingContentId: String? = "",
    val reportingReferenceContentId: String? = "",
    override val uniqueId: String = "$reportingContentId,$reportingReferenceContentId",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<FeedReportingViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<FeedReportingViewEntity>() == this
    }

    override fun viewType() = R.layout.item_feed_reporting

}