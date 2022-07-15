package com.castcle.android.core.custom_view.load_state.item_empty_state_feed

import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemEmptyStateFeedBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class EmptyStateFeedViewHolder(
    private val binding: ItemEmptyStateFeedBinding,
    private val compositeDisposable: CompositeDisposable,
) : CastcleViewHolder<EmptyStateFeedViewEntity>(binding.root) {

    override var item = EmptyStateFeedViewEntity()

    init {
        compositeDisposable += binding.tvRetry.onClick {
            item.action?.invoke() ?: if (item.error == null) {
                item.refreshAction.invoke()
            } else {
                item.retryAction.invoke()
            }
        }
    }

    override fun bind(bindItem: EmptyStateFeedViewEntity) {
        if (BuildConfig.DEBUG) {
            binding.tvRetry.text = if (item.error == null) {
                string(R.string.retry)
            } else {
                "${item.error?.message}"
            }
        }
    }

}