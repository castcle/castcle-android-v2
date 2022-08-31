package com.castcle.android.core.custom_view.load_state.item_empty_state_content

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemEmptyStateContentBinding
import io.reactivex.disposables.CompositeDisposable

class EmptyStateContentViewRenderer : CastcleViewRenderer<EmptyStateContentViewEntity,
    EmptyStateContentViewHolder,
    CastcleListener>(R.layout.item_empty_state_content) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = EmptyStateContentViewHolder(
        ItemEmptyStateContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}