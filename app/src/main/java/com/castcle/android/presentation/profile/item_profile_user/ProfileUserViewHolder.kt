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

package com.castcle.android.presentation.profile.item_profile_user

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemProfileUserBinding
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.profile.ProfileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class ProfileUserViewHolder(
    private val binding: ItemProfileUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ProfileListener,
) : CastcleViewHolder<ProfileUserViewEntity>(binding.root) {

    override var item = ProfileUserViewEntity()

    init {
        compositeDisposable += binding.ivOption.onClick {
            val type = if (item.user.isOwner) {
                OptionDialogType.MyUserOption(userId = item.user.id)
            } else {
                OptionDialogType.OtherUserOption(userId = item.user.id)
            }
            listener.onOptionClicked(type)
        }
        compositeDisposable += binding.ivAvatar.onClick {
            listener.onImageClicked(listOf(item.user.avatar), 0)
        }
        compositeDisposable += binding.ivCover.onClick {
            listener.onImageClicked(listOfNotNull(item.user.cover), 0)
        }
        compositeDisposable += binding.viewFollowing.onClick {
            listener.onFollowingFollowersClicked(isFollowing = true, user = item.user)
        }
        compositeDisposable += binding.viewFollowers.onClick {
            listener.onFollowingFollowersClicked(isFollowing = false, user = item.user)
        }
        compositeDisposable += binding.tvViewProfile.onClick {
            listener.onEditProfileClicked(user = item.user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(user = item.user)
        }
        compositeDisposable += binding.ivAddAvatar.onClick {
            listener.onAddAvatarClick()
        }
        compositeDisposable += binding.ivEditCover.onClick {
            listener.onAddCoverClick()
        }
    }

    override fun bind(bindItem: ProfileUserViewEntity) {
        binding.tvFollower.text = "${item.user.followers.asCount()} ${string(R.string.followers)}"
        binding.tvFollowing.text = "${item.user.following.asCount()} ${string(R.string.following)}"
        binding.tvOverview.isGone = item.user.overview.isNullOrBlank()
        binding.tvOverview.text = item.user.overview?.trim()
        binding.tvDisplayName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivAvatar.loadAvatarImageLocal(item.avatarUpLoad, item.user.avatar.thumbnail)
        binding.ivAddAvatar.isVisible = item.user.isOwner
        binding.ivEditCover.isVisible = item.user.isOwner
        binding.ivCover.loadScaleCenterCrop(
            scale = 12 to 10,
            thumbnailUrl = item.user.cover?.thumbnail,
            url = item.user.cover?.original,
            uri = item.coveUpLoad
        )
        binding.tvViewProfile.text = if (item.user.isOwner) {
            string(R.string.edit_profile)
        } else {
            string(R.string.view_profile)
        }
        with(binding.tvFollow) {
            isGone = item.user.isOwner
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
        onAvatarLoading(item.onUploadAvatar ?: false)
        onCoverLoading(item.onUploadCover ?: false)
    }

    private fun onAvatarLoading(isLoading: Boolean = false) {
        with(binding) {
            vLoading.visibleOrGone(isLoading)
            animationLoading.visibleOrGone(isLoading)
            if (!animationLoading.isAnimating) {
                animationLoading.setAnimation(
                    R.raw.loading_animations
                )
            }
        }
    }

    private fun onCoverLoading(isLoading: Boolean = false) {
        with(binding) {
            animationLoadingCover.visibleOrGone(isLoading)
            if (!animationLoadingCover.isAnimating) {
                animationLoadingCover.setAnimation(
                    R.raw.loading_animations
                )
            }
        }
    }

}