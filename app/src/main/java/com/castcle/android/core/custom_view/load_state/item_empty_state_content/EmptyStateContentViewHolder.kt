package com.castcle.android.core.custom_view.load_state.item_empty_state_content

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.databinding.ItemEmptyStateContentBinding

class EmptyStateContentViewHolder(
    binding: ItemEmptyStateContentBinding
) : CastcleViewHolder<EmptyStateContentViewEntity>(binding.root) {

    override var item = EmptyStateContentViewEntity()

}