package com.castcle.android.core.base.recyclerview

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

class CastcleAdapter(
    private val listener: CastcleListener,
    private val disposable: CompositeDisposable,
) : ListAdapter<CastcleViewEntity, RecyclerView.ViewHolder>(CastcleDiffCallback) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType() ?: -1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also { viewRenderers[it.viewType()].internalBind(holder, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewRenderers[viewType].internalCreateViewHolder(parent, listener, disposable)
    }

    fun registerRenderer(
        renderer: CastcleViewRenderer<
            out CastcleViewEntity,
            out CastcleViewHolder<out CastcleViewEntity>,
            out CastcleListener>
    ) {
        if (viewRenderers.get(renderer.viewType) == null) {
            viewRenderers.put(renderer.viewType, renderer)
        }
    }

    fun submitList(item: CastcleViewEntity) {
        submitList(listOf(item))
    }

    private val viewRenderers = SparseArray<CastcleViewRenderer<
        out CastcleViewEntity,
        out CastcleViewHolder<out CastcleViewEntity>,
        out CastcleListener>>()

}