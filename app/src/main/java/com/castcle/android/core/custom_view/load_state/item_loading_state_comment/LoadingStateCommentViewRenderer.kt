package com.castcle.android.core.custom_view.load_state.item_loading_state_comment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoadingStateCommentBinding
import io.reactivex.disposables.CompositeDisposable

class LoadingStateCommentViewRenderer : CastcleViewRenderer<LoadingStateCommentViewEntity,
    LoadingStateCommentViewHolder,
    CastcleListener>(R.layout.item_loading_state_comment) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ) = LoadingStateCommentViewHolder(
        ItemLoadingStateCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}