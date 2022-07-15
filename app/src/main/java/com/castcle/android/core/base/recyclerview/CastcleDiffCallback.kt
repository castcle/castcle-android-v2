package com.castcle.android.core.base.recyclerview

import androidx.recyclerview.widget.DiffUtil

object CastcleDiffCallback : DiffUtil.ItemCallback<CastcleViewEntity>() {

    override fun areItemsTheSame(
        oldItem: CastcleViewEntity,
        newItem: CastcleViewEntity
    ): Boolean = oldItem.sameAs(isSameItem = true, target = newItem)

    override fun areContentsTheSame(
        oldItem: CastcleViewEntity,
        newItem: CastcleViewEntity
    ): Boolean = oldItem.sameAs(isSameItem = false, target = newItem)

}