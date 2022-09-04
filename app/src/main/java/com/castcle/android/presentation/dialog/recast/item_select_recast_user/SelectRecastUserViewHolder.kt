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

package com.castcle.android.presentation.dialog.recast.item_select_recast_user

import androidx.core.view.isInvisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSelectRecastUserBinding
import com.castcle.android.presentation.dialog.recast.RecastDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SelectRecastUserViewHolder(
    private val binding: ItemSelectRecastUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RecastDialogListener,
) : CastcleViewHolder<SelectRecastUserViewEntity>(binding.root) {

    override var item = SelectRecastUserViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user.id)
        }
    }

    override fun bind(bindItem: SelectRecastUserViewEntity) {
        binding.ivCheck.isInvisible = !item.isSelected
        binding.tvDisplayName.text = item.user.displayName
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.root.setBackgroundColor(
            if (item.isSelected) {
                color(R.color.black_background_3)
            } else {
                color(R.color.black_background_1)
            }
        )
    }

}