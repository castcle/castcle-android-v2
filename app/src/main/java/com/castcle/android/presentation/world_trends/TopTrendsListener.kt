package com.castcle.android.presentation.world_trends

import com.castcle.android.core.base.recyclerview.CastcleListener

interface TopTrendsListener : CastcleListener {
    fun onSearchClicked()
    fun onTrendClicked(keyword: String)
}