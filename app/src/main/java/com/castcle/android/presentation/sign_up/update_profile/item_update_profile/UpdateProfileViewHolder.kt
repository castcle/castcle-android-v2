package com.castcle.android.presentation.sign_up.update_profile.item_update_profile

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemUpdateProfileBinding
import com.castcle.android.presentation.sign_up.update_profile.UpdateProfileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 7/9/2022 AD at 11:47.

class UpdateProfileViewHolder(
    val binding: ItemUpdateProfileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: UpdateProfileListener,
) : CastcleViewHolder<UpdateProfileViewEntity>(binding.root) {

    override var item = UpdateProfileViewEntity()

    init {
        compositeDisposable += binding.ivAddAvatar.onClick {
            listener.onUpdateAvatar()
        }

        compositeDisposable += binding.ivEditCover.onClick {
            listener.onUpdateCover()
        }

        compositeDisposable += binding.btNext.onClick {
            listener.onNext()
        }
    }

    override fun bind(bindItem: UpdateProfileViewEntity) {
        super.bind(bindItem)
        with(binding) {
            binding.btNext.run {
                isEnabled = true
            }
            bindItem.userEntity.let {
                tvCastcleId.text = it.castcleId
                tvCastcleName.text = it.displayName
                ivAvatarProfile.loadAvatarImageLocal(
                    imageUri = bindItem.avatarUpLoad,
                    imageUrl = it.avatar.original
                )

                ivCreatePass.loadScaleCenterCrop(
                    scale = 12 to 10,
                    thumbnailUrl = it.cover?.thumbnail,
                    uri = bindItem.coveUpLoad, url = it.cover?.original?.ifBlank { null }
                )
            }
            bindItem.onUploadAvatar?.let {
                onAvatarLoading(it)
            }
            bindItem.onUploadCover?.let {
                onCoverLoading(it)
            }
        }
    }

    private fun onAvatarLoading(isLoading: Boolean = false) {
        with(binding) {
            vLoading.visibleOrGone(isLoading)
            animationLoading.visibleOrGone(isLoading)
            animationLoading.setAnimation(
                R.raw.loading_animations
            )
        }
    }

    private fun onCoverLoading(isLoading: Boolean = false) {
        with(binding) {
            animationLoadingCover.visibleOrGone(isLoading)
            animationLoadingCover.setAnimation(
                R.raw.loading_animations
            )
        }
    }
}