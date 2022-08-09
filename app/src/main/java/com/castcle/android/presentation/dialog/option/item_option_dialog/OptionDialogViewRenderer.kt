package com.castcle.android.presentation.dialog.option.item_option_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemOptionDialogBinding
import com.castcle.android.presentation.dialog.option.OptionDialogListener
import io.reactivex.disposables.CompositeDisposable

class OptionDialogViewRenderer : CastcleViewRenderer<OptionDialogViewEntity,
    OptionDialogViewHolder,
    OptionDialogListener>(R.layout.item_option_dialog) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: OptionDialogListener,
        compositeDisposable: CompositeDisposable
    ) = OptionDialogViewHolder(
        ItemOptionDialogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}