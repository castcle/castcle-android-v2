package com.castcle.android.presentation.dialog.item_dialog_option

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemDialogOptionBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class DialogOptionViewHolder(
    private val binding: ItemDialogOptionBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: DialogOptionViewListener,
) : CastcleViewHolder<DialogOptionViewEntity>(binding.root) {

    override var item = DialogOptionViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onOptionClicked(item.title)
        }
    }

    override fun bind(bindItem: DialogOptionViewEntity) {
        binding.ivIcon.setImageResource(item.icon)
        binding.tvTitle.text = string(item.title)
    }

}