package com.castcle.android.core.custom_view.load_state.item_loading_state_cast

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingStateCastBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingStateCastViewRenderer : CastcleViewRenderer<LoadingStateCastViewEntity,
    LoadingStateCastViewHolder,
    CastcleListener>(R.layout.item_loading_state_cast) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingStateCastViewHolder(
        ItemLoadingStateCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}