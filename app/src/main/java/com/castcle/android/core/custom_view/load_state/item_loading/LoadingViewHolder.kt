package com.castcle.android.core.custom_view.load_state.item_loading

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.databinding.ItemLoadingBinding

class LoadingViewHolder(binding: ItemLoadingBinding) :
    CastcleViewHolder<LoadingViewEntity>(binding.root) {

    override var item = LoadingViewEntity()

}