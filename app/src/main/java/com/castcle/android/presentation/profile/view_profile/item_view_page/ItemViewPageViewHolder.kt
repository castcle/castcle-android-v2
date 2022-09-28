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

package com.castcle.android.presentation.profile.view_profile.item_view_page

import android.annotation.SuppressLint
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemViewPageBinding
import com.castcle.android.presentation.profile.view_profile.ViewProfileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class ItemViewPageViewHolder(
    private val binding: ItemViewPageBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ViewProfileListener,
) : CastcleViewHolder<ItemViewPageViewEntity>(binding.root) {

    override var item = ItemViewPageViewEntity()

    init {
        compositeDisposable += binding.itLinkFacebook.onClick {
            item.user.linkFacebook?.let {
                listener.onSocialLinkClick(it)
            }
        }

        compositeDisposable += binding.itLinkMedium.onClick {
            item.user.linkMedium?.let {
                listener.onSocialLinkClick(it)
            }
        }

        compositeDisposable += binding.itLinkTwitter.onClick {
            item.user.linkTwitter?.let {
                listener.onSocialLinkClick(it)
            }
        }

        compositeDisposable += binding.itLinkYouTube.onClick {
            item.user.linkYoutube?.let {
                listener.onSocialLinkClick(it)
            }
        }

        compositeDisposable += binding.itLinkWeb.onClick {
            item.user.linkWebsite?.let {
                listener.onSocialLinkClick(it)
            }
        }
    }

    override fun bind(bindItem: ItemViewPageViewEntity) {
        with(binding) {
            tvCastcleName.text = item.user.displayName
            tvCastcleId.text = item.user.castcleId
            ivAvatar.loadAvatarImageLocal(imageUrl = item.user.avatar.thumbnail)
            ivCover.loadScaleCenterCrop(
                scale = 12 to 10,
                thumbnailUrl = item.user.cover?.thumbnail,
                url = item.user.cover?.original
            )

            item.user.let {
                with(itemDetailProfile) {
                    clContractEmail.gone()
                    it.contactEmail?.let {
                        clContractEmail.visible()
                        tvEmailDescription.text = it
                    }

                    clContractNumber.gone()
                    it.contactNumber?.let {
                        clContractNumber.gone()
                        tvNumberDescription.text = it
                    }

                    vtPageCreateDate.text = it.createdAt?.toCreateDate()
                }

                tvLinks.visibleOrGone(
                    it.linkFacebook != null || it.linkTwitter != null ||
                        it.linkYoutube != null || it.linkMedium != null || it.linkWebsite != null
                )

                with(itLinkFacebook) {
                    it.linkFacebook?.let {
                        groupFacebook.visibleOrGone(it != schemeHttps)
                        text = it
                    }
                }

                with(itLinkMedium) {
                    it.linkMedium?.let {
                        groupMedium.visibleOrGone(it != schemeHttps)
                        text = it
                    }
                }

                with(itLinkTwitter) {
                    it.linkTwitter?.let {
                        groupTwitter.visibleOrGone(it != schemeHttps)
                        text = it
                    }
                }

                with(itLinkYouTube) {
                    it.linkYoutube?.let {
                        groupYoutube.visibleOrGone(it != schemeHttps)
                        text = it
                    }
                }

                with(itLinkWeb) {
                    it.linkWebsite?.let {
                        groupWeb.visibleOrGone(it != schemeHttps)
                        text = it
                    }
                }
            }
        }
    }

}