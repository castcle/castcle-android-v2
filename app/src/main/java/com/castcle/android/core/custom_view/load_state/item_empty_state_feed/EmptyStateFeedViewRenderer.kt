package com.castcle.android.core.custom_view.load_state.item_empty_state_feed

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemEmptyStateFeedBinding
import io.reactivex.disposables.CompositeDisposable

class EmptyStateFeedViewRenderer : CastcleViewRenderer<EmptyStateFeedViewEntity,
    EmptyStateFeedViewHolder,
    CastcleListener>(R.layout.item_empty_state_feed) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = EmptyStateFeedViewHolder(
        ItemEmptyStateFeedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable
    )

}