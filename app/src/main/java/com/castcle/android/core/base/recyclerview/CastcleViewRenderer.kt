package com.castcle.android.core.base.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
abstract class CastcleViewRenderer<Item : CastcleViewEntity,
    ViewHolder : CastcleViewHolder<Item>,
    Listener : CastcleListener>(val viewType: Int) {

    abstract fun createViewHolder(
        parent: ViewGroup,
        listener: Listener,
        compositeDisposable: CompositeDisposable,
    ): RecyclerView.ViewHolder

    fun internalCreateViewHolder(
        parent: ViewGroup,
        listener: CastcleListener,
        compositeDisposable: CompositeDisposable,
    ): RecyclerView.ViewHolder {
        val castedListener = listener as? Listener
            ?: throw IllegalStateException("Type not valid of instance ${listener::class.simpleName}")
        return createViewHolder(parent, castedListener, compositeDisposable)
    }

    fun internalBind(
        viewHolder: RecyclerView.ViewHolder,
        item: CastcleViewEntity,
    ) {
        val castedViewHolder = viewHolder as? ViewHolder
            ?: throw IllegalStateException("Type not valid of instance ${viewHolder::class.simpleName}")
        item as? Item
            ?: throw IllegalStateException("Type not valid of instance ${item::class.simpleName}")
        castedViewHolder.bindItem(item)
        castedViewHolder.bind(item)
    }

}