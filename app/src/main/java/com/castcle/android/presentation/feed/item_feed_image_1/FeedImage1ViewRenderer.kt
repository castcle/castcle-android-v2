package com.castcle.android.presentation.feed.item_feed_image_1

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedImage1Binding
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedImage1ViewRenderer(
    private val referenceType: CastType? = null
) : CastcleViewRenderer<FeedImage1ViewEntity,
    FeedImage1ViewHolder,
    FeedListener>(R.layout.item_feed_image_1) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedImage1ViewHolder(
        ItemFeedImage1Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, referenceType
    )

}