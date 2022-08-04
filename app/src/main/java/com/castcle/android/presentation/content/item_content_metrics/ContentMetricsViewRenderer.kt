package com.castcle.android.presentation.content.item_content_metrics

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemContentMetricsBinding
import com.castcle.android.presentation.content.ContentListener
import io.reactivex.disposables.CompositeDisposable

class ContentMetricsViewRenderer : CastcleViewRenderer<ContentMetricsViewEntity,
    ContentMetricsViewHolder,
    ContentListener>(R.layout.item_content_metrics) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ContentListener,
        compositeDisposable: CompositeDisposable
    ) = ContentMetricsViewHolder(
        ItemContentMetricsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}