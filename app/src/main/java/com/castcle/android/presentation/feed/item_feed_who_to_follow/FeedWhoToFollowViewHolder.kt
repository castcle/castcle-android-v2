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

package com.castcle.android.presentation.feed.item_feed_who_to_follow

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedWhoToFollowBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedWhoToFollowViewHolder(
    private val binding: ItemFeedWhoToFollowBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedWhoToFollowViewEntity>(binding.root) {

    override var item = FeedWhoToFollowViewEntity()

    init {
        compositeDisposable += binding.clUser1.onClick {
            item.user1?.also(listener::onUserClicked)
        }
        compositeDisposable += binding.clUser2.onClick {
            item.user2?.also(listener::onUserClicked)
        }
        compositeDisposable += binding.tvFollow1.onClick {
            item.user1?.also(listener::onFollowClicked)
        }
        compositeDisposable += binding.tvFollow2.onClick {
            item.user2?.also(listener::onFollowClicked)
        }
        compositeDisposable += binding.tvShowMore.onClick {
            listener.onWhoToFollowClicked()
        }
    }

    override fun bind(bindItem: FeedWhoToFollowViewEntity) {
        binding.clUser1.isVisible = item.user1 != null
        binding.clUser2.isVisible = item.user2 != null
        binding.ivAvatar1.loadAvatarImage(item.user1?.avatar?.thumbnail)
        binding.ivAvatar2.loadAvatarImage(item.user2?.avatar?.thumbnail)
        binding.tvUserName1.text = item.user1?.displayName
        binding.tvUserName2.text = item.user2?.displayName
        binding.tvCastcleId1.text = item.user1?.castcleId
        binding.tvCastcleId2.text = item.user2?.castcleId
        binding.ivOfficial1.isVisible = item.user1?.verifiedOfficial == true
        binding.ivOfficial2.isVisible = item.user2?.verifiedOfficial == true
        binding.tvDescription1.text = item.user1?.overview
        binding.tvDescription2.text = item.user2?.overview
        binding.tvDescription1.isVisible = !item.user1?.overview.isNullOrBlank()
        binding.tvDescription2.isVisible = !item.user2?.overview.isNullOrBlank()
        with(binding.tvFollow1) {
            isVisible = item.user1?.isOwner == false
            text = if (item.user1?.followed == true) {
                string(R.string.followed)
            } else {
                string(R.string.follow)
            }
            background = if (item.user1?.followed == true) {
                drawable(R.drawable.bg_corner_16dp)
            } else {
                drawable(R.drawable.bg_outline_corner_16dp)
            }
            setTextColor(
                if (item.user1?.followed == true) {
                    color(R.color.white)
                } else {
                    color(R.color.blue)
                }
            )
        }
        with(binding.tvFollow2) {
            isVisible = item.user2?.isOwner == false
            text = if (item.user2?.followed == true) {
                string(R.string.followed)
            } else {
                string(R.string.follow)
            }
            background = if (item.user2?.followed == true) {
                drawable(R.drawable.bg_corner_16dp)
            } else {
                drawable(R.drawable.bg_outline_corner_16dp)
            }
            setTextColor(
                if (item.user2?.followed == true) {
                    color(R.color.white)
                } else {
                    color(R.color.blue)
                }
            )
        }
    }

}