package com.castcle.android.core.custom_view.load_state.item_loading_state_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingStateProfileBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingStateProfileViewRenderer : CastcleViewRenderer<LoadingStateProfileViewEntity,
    LoadingStateProfileViewHolder,
    CastcleListener>(R.layout.item_loading_state_profile) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingStateProfileViewHolder(
        ItemLoadingStateProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}