package com.castcle.android.presentation.feed.item_feed_text

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedTextBinding
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedTextViewRenderer(
    private val referenceType: CastType? = null
) : CastcleViewRenderer<FeedTextViewEntity,
    FeedTextViewHolder,
    FeedListener>(R.layout.item_feed_text) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable,
    ) = FeedTextViewHolder(
        ItemFeedTextBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, referenceType
    )

}