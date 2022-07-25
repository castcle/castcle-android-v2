package com.castcle.android.core.custom_view.load_state.item_empty_state_search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemEmptyStateSearchBinding
import io.reactivex.disposables.CompositeDisposable

class EmptyStateSearchViewRenderer : CastcleViewRenderer<EmptyStateSearchViewEntity,
    EmptyStateSearchViewHolder,
    CastcleListener>(R.layout.item_empty_state_search) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = EmptyStateSearchViewHolder(
        ItemEmptyStateSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}