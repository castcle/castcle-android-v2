package com.castcle.android.core.custom_view.load_state.item_loading_state_user

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingStateUserBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingStateUserViewRenderer : CastcleViewRenderer<LoadingStateUserViewEntity,
    LoadingStateUserViewHolder,
    CastcleListener>(R.layout.item_loading_state_user) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingStateUserViewHolder(
        ItemLoadingStateUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}