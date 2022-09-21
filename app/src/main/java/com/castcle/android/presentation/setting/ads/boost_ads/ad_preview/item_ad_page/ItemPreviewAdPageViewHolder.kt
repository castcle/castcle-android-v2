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

package com.castcle.android.presentation.setting.ads.boost_ads.ad_preview.item_ad_page

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAdPreviewBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.setting.ads.boost_ads.BoostAdsListener
import io.reactivex.disposables.CompositeDisposable

class ItemPreviewAdPageViewHolder(
    private val binding: ItemAdPreviewBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: BoostAdsListener,
) : CastcleViewHolder<ItemPreviewAdPageViewEntity>(binding.root), UserBarListener {

    override var item = ItemPreviewAdPageViewEntity()

    override fun bind(bindItem: ItemPreviewAdPageViewEntity) {
        with(binding) {
            bindItem.userEntity.let { userEntity ->
                userBar.bind(
                    cast = CastEntity(type = CastType.AdsContent),
                    userEntity,
                    listener = this@ItemPreviewAdPageViewHolder,
                    false
                )
                castcleTextView.text = item.campaignMessage
                with(layoutAdContent) {
                    userEntity.avatar.original.let {
                        ivAvatar.loadAvatarImage(it)
                    }
                    userEntity.cover?.let {
                        ivAdCover.loadScaleCenterCropWithRoundedCorners(
                            thumbnailUrl = it.thumbnail,
                            uri = null,
                            url = it.original,
                            viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp))
                        )
                    }
                    tvCastcleName.text = userEntity.displayName
                    tvCampaignMessage.text = userEntity.overview
                }
            }
        }
    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {

    }

    override fun onFollowClicked(user: UserEntity) {

    }

    override fun onUserClicked(user: UserEntity) {

    }
}
