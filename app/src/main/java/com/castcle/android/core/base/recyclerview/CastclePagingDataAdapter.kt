package com.castcle.android.core.base.recyclerview

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.custom_view.load_state.item_loading_state_cast.LoadingStateCastViewRenderer
import com.castcle.android.core.error.MissingViewRendererException
import io.reactivex.disposables.CompositeDisposable

class CastclePagingDataAdapter(
    private val listener: CastcleListener,
    private val disposable: CompositeDisposable,
) : PagingDataAdapter<CastcleViewEntity, RecyclerView.ViewHolder>(CastcleDiffCallback) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT
    }

    private var defaultItem: CastcleViewRenderer<
        out CastcleViewEntity,
        out CastcleViewHolder<out CastcleViewEntity>,
        out CastcleListener> = LoadingStateCastViewRenderer()

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType() ?: defaultItem.viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also { viewRenderers[it.viewType()].internalBind(holder, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return try {
            viewRenderers[viewType].internalCreateViewHolder(parent, listener, disposable)
        } catch (exception: Exception) {
            throw MissingViewRendererException(
                viewId = viewType,
                viewResourceName = parent.context.resources.getResourceEntryName(viewType),
            )
        }
    }

    fun registerRenderer(
        renderer: CastcleViewRenderer<
            out CastcleViewEntity,
            out CastcleViewHolder<out CastcleViewEntity>,
            out CastcleListener>,
        isDefaultItem: Boolean = false,
    ) {
        if (isDefaultItem) {
            defaultItem = renderer
        }
        if (viewRenderers.get(renderer.viewType) == null) {
            viewRenderers.put(renderer.viewType, renderer)
        }
    }

    private val viewRenderers = SparseArray<CastcleViewRenderer<
        out CastcleViewEntity,
        out CastcleViewHolder<out CastcleViewEntity>,
        out CastcleListener>>().apply {
        put(defaultItem.viewType, defaultItem)
    }

}