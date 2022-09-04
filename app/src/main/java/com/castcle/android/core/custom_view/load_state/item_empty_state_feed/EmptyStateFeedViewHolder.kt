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

package com.castcle.android.core.custom_view.load_state.item_empty_state_feed

import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemEmptyStateFeedBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class EmptyStateFeedViewHolder(
    private val binding: ItemEmptyStateFeedBinding,
    private val compositeDisposable: CompositeDisposable,
) : CastcleViewHolder<EmptyStateFeedViewEntity>(binding.root) {

    override var item = EmptyStateFeedViewEntity()

    init {
        compositeDisposable += binding.tvDescription.onClick {
            item.action()
        }
    }

    override fun bind(bindItem: EmptyStateFeedViewEntity) {
        binding.tvDescription.text = if (BuildConfig.DEBUG && item.error != null) {
            item.error?.message ?: string(R.string.retry)
        } else {
            string(R.string.retry)
        }
    }

}