package com.castcle.android.presentation.search.top_trends.item_top_trends_title

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

class TopTrendsTitleViewEntity(
    override val uniqueId: String = "${R.layout.item_top_trends_title}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<TopTrendsTitleViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<TopTrendsTitleViewEntity>() == this
    }

    override fun viewType() = R.layout.item_top_trends_title

}