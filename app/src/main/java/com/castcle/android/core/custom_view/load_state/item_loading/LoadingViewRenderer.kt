package com.castcle.android.core.custom_view.load_state.item_loading

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingViewRenderer : CastcleViewRenderer<LoadingViewEntity,
    LoadingViewHolder,
    CastcleListener>(R.layout.item_loading) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingViewHolder(
        ItemLoadingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}