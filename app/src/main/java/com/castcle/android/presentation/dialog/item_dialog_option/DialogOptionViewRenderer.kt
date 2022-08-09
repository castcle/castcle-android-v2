package com.castcle.android.presentation.dialog.item_dialog_option

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemDialogOptionBinding
import io.reactivex.disposables.CompositeDisposable

class DialogOptionViewRenderer : CastcleViewRenderer<DialogOptionViewEntity,
    DialogOptionViewHolder,
    DialogOptionViewListener>(R.layout.item_dialog_option) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: DialogOptionViewListener,
        compositeDisposable: CompositeDisposable
    ) = DialogOptionViewHolder(
        ItemDialogOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}