package com.castcle.android.core.custom_view.load_state.item_empty_state_profile

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.databinding.ItemEmptyStateProfileBinding

class EmptyStateProfileViewHolder(
    binding: ItemEmptyStateProfileBinding,
) : CastcleViewHolder<EmptyStateProfileViewEntity>(binding.root) {

    override var item = EmptyStateProfileViewEntity()

}