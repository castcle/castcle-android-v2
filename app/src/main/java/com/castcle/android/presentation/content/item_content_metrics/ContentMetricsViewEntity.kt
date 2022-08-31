package com.castcle.android.presentation.content.item_content_metrics

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class ContentMetricsViewEntity(
    val contentId: String = "",
    val likeCount: Int = 0,
    val quoteCount: Int = 0,
    val recastCount: Int = 0,
    override val uniqueId: String = "${R.layout.item_content_metrics}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<ContentMetricsViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<ContentMetricsViewEntity>() == this
    }

    override fun viewType() = R.layout.item_content_metrics

}