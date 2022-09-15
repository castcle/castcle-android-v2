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

package com.castcle.android.presentation.who_to_follow.item_who_to_follow

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWhoToFollowBinding
import com.castcle.android.presentation.who_to_follow.WhoToFollowListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WhoToFollowViewHolder(
    private val binding: ItemWhoToFollowBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WhoToFollowListener,
) : CastcleViewHolder<WhoToFollowViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(item.user)
        }
    }

    override var item = WhoToFollowViewEntity()

    override fun bind(bindItem: WhoToFollowViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvUserName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivOfficial.isVisible = item.user.verifiedOfficial == true
        binding.tvDescription.text = item.user.overview
        binding.tvDescription.isVisible = !item.user.overview.isNullOrBlank()
        with(binding.tvFollow) {
            isVisible = !item.user.isOwner
            text = if (item.user.followed) {
                string(R.string.followed)
            } else {
                string(R.string.follow)
            }
            background = if (item.user.followed) {
                drawable(R.drawable.bg_corner_16dp)
            } else {
                drawable(R.drawable.bg_outline_corner_16dp)
            }
            setTextColor(
                if (item.user.followed) {
                    color(R.color.white)
                } else {
                    color(R.color.blue)
                }
            )
        }
    }

}