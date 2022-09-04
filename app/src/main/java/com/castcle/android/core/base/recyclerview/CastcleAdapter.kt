/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

package com.castcle.android.core.base.recyclerview

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.error.MissingViewRendererException
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