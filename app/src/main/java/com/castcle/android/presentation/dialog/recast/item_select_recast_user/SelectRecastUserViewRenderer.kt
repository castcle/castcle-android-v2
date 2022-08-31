package com.castcle.android.presentation.dialog.recast.item_select_recast_user

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSelectRecastUserBinding
import com.castcle.android.presentation.dialog.recast.RecastDialogListener
import io.reactivex.disposables.CompositeDisposable

class SelectRecastUserViewRenderer : CastcleViewRenderer<SelectRecastUserViewEntity,
    SelectRecastUserViewHolder,
    RecastDialogListener>(R.layout.item_select_recast_user) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: RecastDialogListener,
        compositeDisposable: CompositeDisposable
    ) = SelectRecastUserViewHolder(
        ItemSelectRecastUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}