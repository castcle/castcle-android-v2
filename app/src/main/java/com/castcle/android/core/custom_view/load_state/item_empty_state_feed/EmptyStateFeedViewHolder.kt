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
        compositeDisposable += binding.tvDescription.onClick {
            item.action()
        }
    }

    override fun bind(bindItem: EmptyStateFeedViewEntity) {
        binding.tvDescription.text = if (BuildConfig.DEBUG && item.error != null) {
            item.error?.message ?: string(R.string.retry)
        } else {
            string(R.string.retry)
        }
    }

}