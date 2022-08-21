package com.castcle.android.core.custom_view.load_state.item_empty_state_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemEmptyStateProfileBinding
import io.reactivex.disposables.CompositeDisposable

class EmptyStateProfileViewRenderer : CastcleViewRenderer<EmptyStateProfileViewEntity,
    EmptyStateProfileViewHolder,
    CastcleListener>(R.layout.item_empty_state_profile) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = EmptyStateProfileViewHolder(
        ItemEmptyStateProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}