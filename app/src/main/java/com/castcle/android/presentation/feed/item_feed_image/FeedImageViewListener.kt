package com.castcle.android.presentation.feed.item_feed_image

import com.castcle.android.core.base.recyclerview.CastcleListener

interface FeedImageViewListener : CastcleListener {
    fun onChildImageClicked(position: Int) = Unit
    fun onChildImageDeleteClicked(position: Int) = Unit
}