package com.castcle.android.presentation.dialog.option.item_option_dialog

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemOptionDialogBinding
import com.castcle.android.presentation.dialog.option.OptionDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class OptionDialogViewHolder(
    private val binding: ItemOptionDialogBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: OptionDialogListener,
) : CastcleViewHolder<OptionDialogViewEntity>(binding.root) {

    override var item = OptionDialogViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onOptionClicked(item.title)
        }
    }

    override fun bind(bindItem: OptionDialogViewEntity) {
        binding.ivIcon.setImageResource(item.icon)
        binding.tvTitle.text = string(item.title)
        binding.root.background = if (bindingAdapterPosition == 0) {
            drawable(R.drawable.bg_corner_top_16dp)
        } else {
            drawable(R.drawable.bg_rectangle)
        }
    }

}