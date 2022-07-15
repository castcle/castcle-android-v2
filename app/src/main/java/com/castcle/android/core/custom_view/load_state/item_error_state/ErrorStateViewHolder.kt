package com.castcle.android.core.custom_view.load_state.item_error_state

import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemErrorStateBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ErrorStateViewHolder(
    private val binding: ItemErrorStateBinding,
    private val compositeDisposable: CompositeDisposable
) : CastcleViewHolder<ErrorStateViewEntity>(binding.root) {

    override var item = ErrorStateViewEntity()

    init {
        compositeDisposable += binding.retry.onClick {
            item.action?.invoke() ?: item.retryAction.invoke()
        }
    }

    override fun bind(bindItem: ErrorStateViewEntity) {
        binding.tvError.text = if (BuildConfig.DEBUG && item.error != null) {
            item.error.toString()
        } else {
            string(R.string.error_base)
        }
    }

}