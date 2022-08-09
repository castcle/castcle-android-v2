package com.castcle.android.presentation.feed

sealed class FeedDisplayType {
    object NewCast : FeedDisplayType()
    object Normal : FeedDisplayType()
    object QuoteCast : FeedDisplayType()
    object Recast : FeedDisplayType()
}