package com.castcle.android.presentation.feed.item_feed_image_3

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemFeedImage3Binding
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable

class FeedImage3ViewRenderer(
    private val referenceType: CastType? = null
) : CastcleViewRenderer<FeedImage3ViewEntity,
    FeedImage3ViewHolder,
    FeedListener>(R.layout.item_feed_image_3) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: FeedListener,
        compositeDisposable: CompositeDisposable
    ) = FeedImage3ViewHolder(
        ItemFeedImage3Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener, referenceType
    )

}