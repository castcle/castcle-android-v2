package com.castcle.android.core.base.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CastcleViewHolder<Item : CastcleViewEntity>(view: View) :
    RecyclerView.ViewHolder(view) {

    abstract var item: Item

    open fun bindItem(bindItem: Item) {
        item = bindItem
    }

    open fun bind(bindItem: Item) = Unit

}