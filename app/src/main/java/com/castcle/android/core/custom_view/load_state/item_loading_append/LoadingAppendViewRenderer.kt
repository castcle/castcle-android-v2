package com.castcle.android.core.custom_view.load_state.item_loading_append

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingAppendBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingAppendViewRenderer : CastcleViewRenderer<LoadingAppendViewEntity,
    LoadingAppendViewHolder,
    CastcleListener>(R.layout.item_loading_append) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingAppendViewHolder(
        ItemLoadingAppendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}