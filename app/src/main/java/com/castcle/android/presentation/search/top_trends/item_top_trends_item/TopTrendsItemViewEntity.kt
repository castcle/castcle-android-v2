package com.castcle.android.presentation.search.top_trends.item_top_trends_item

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast
import com.castcle.android.domain.search.entity.SearchSuggestionHashtagEntity

class TopTrendsItemViewEntity(
    val trend: SearchSuggestionHashtagEntity = SearchSuggestionHashtagEntity(),
    override val uniqueId: String = "",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<TopTrendsItemViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<TopTrendsItemViewEntity>() == this
    }

    override fun viewType() = R.layout.item_top_trends_item

    companion object {
        fun map(entity: SearchSuggestionHashtagEntity): CastcleViewEntity = TopTrendsItemViewEntity(
            trend = entity,
            uniqueId = entity.slug,
        )
    }

}