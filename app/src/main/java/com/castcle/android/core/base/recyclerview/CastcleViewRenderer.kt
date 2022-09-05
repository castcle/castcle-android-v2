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