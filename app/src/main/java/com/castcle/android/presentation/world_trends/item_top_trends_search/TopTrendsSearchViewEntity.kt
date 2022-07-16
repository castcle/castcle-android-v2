package com.castcle.android.presentation.world_trends.item_top_trends_search

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

class TopTrendsSearchViewEntity(
    override val uniqueId: String = "${R.layout.item_top_trends_search}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<TopTrendsSearchViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<TopTrendsSearchViewEntity>() == this
    }

    override fun viewType() = R.layout.item_top_trends_search

}