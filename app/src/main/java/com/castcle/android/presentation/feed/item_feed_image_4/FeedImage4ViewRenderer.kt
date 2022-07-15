package com.castcle.android.presentation.feed.item_feed_image_4

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedImage4Binding
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedImage4ViewRenderer(
    private val referenceType: CastType? = null
) : CastcleViewRenderer<FeedImage4ViewEntity,
    FeedImage4ViewHolder,
    FeedListener>(R.layout.item_feed_image_4) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedImage4ViewHolder(
        ItemFeedImage4Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, referenceType
    )

}