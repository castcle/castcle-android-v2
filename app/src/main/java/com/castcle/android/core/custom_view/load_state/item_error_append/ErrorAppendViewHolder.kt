package com.castcle.android.core.custom_view.load_state.item_error_append

import androidx.core.view.isGone
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemErrorAppendBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ErrorAppendViewHolder(
    private val binding: ItemErrorAppendBinding,
    private val compositeDisposable: CompositeDisposable,
) : CastcleViewHolder<ErrorAppendViewEntity>(binding.root) {

    override var item = ErrorAppendViewEntity()

    init {
        compositeDisposable += binding.retry.onClick {
            item.action()
        }
    }

    override fun bind(bindItem: ErrorAppendViewEntity) {
        binding.tvError.isGone = item.errorMessage.isNullOrBlank()
        binding.tvError.text = item.errorMessage.orEmpty()
    }

}