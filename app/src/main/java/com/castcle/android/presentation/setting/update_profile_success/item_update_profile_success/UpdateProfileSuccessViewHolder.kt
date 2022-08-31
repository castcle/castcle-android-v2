package com.castcle.android.presentation.setting.update_profile_success.item_update_profile_success

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemUpdateProfileSuccessBinding
import com.castcle.android.presentation.setting.update_profile_success.UpdateProfileSuccessListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class UpdateProfileSuccessViewHolder(
    private val binding: ItemUpdateProfileSuccessBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: UpdateProfileSuccessListener,
) : CastcleViewHolder<UpdateProfileSuccessViewEntity>(binding.root) {

    override var item = UpdateProfileSuccessViewEntity()

    init {
        compositeDisposable += binding.tvClose.onClick {
            listener.onCloseClicked()
        }
    }

    override fun bind(bindItem: UpdateProfileSuccessViewEntity) {
        binding.ivIcon.setImageResource(item.icon)
        binding.tvClose.text = item.close
        binding.tvTitle.text = item.title
        binding.tvDescription.text = item.description
    }

}