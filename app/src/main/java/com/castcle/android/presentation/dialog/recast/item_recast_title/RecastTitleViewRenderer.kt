package com.castcle.android.presentation.dialog.recast.item_recast_title

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemRecastTitleBinding
import com.castcle.android.presentation.dialog.recast.RecastDialogListener
import io.reactivex.disposables.CompositeDisposable

class RecastTitleViewRenderer : CastcleViewRenderer<RecastTitleViewEntity,
    RecastTitleViewHolder,
    RecastDialogListener>(R.layout.item_recast_title) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: RecastDialogListener,
        compositeDisposable: CompositeDisposable
    ) = RecastTitleViewHolder(
        ItemRecastTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}