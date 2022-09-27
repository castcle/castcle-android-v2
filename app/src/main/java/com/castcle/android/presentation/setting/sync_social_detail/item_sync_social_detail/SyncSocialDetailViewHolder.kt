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

package com.castcle.android.presentation.setting.sync_social_detail.item_sync_social_detail

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSyncSocialDetailBinding
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.presentation.setting.sync_social_detail.SyncSocialDetailListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class SyncSocialDetailViewHolder(
    private val binding: ItemSyncSocialDetailBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SyncSocialDetailListener,
) : CastcleViewHolder<SyncSocialDetailViewEntity>(binding.root) {

    override var item = SyncSocialDetailViewEntity()

    init {
        compositeDisposable += binding.tvDisconnect.onClick {
            listener.onDisconnectClicked(item.user.id)
        }
        binding.swAutoPost.setOnCheckedChangeListener { _, isCheck ->
            if (item.syncSocial.autoPost == isCheck) {
                return@setOnCheckedChangeListener
            } else {
                listener.onAutoPostClicked(isCheck, item.user.id)
            }
        }
    }

    override fun bind(bindItem: SyncSocialDetailViewEntity) {
        binding.ivAvatarProvider.loadAvatarImage(item.syncSocial.avatar)
        binding.tvProviderName.text = item.syncSocial.displayName
        binding.tvProviderId.text = "@${item.syncSocial.userName}"
        binding.tvProviderId.isVisible = item.syncSocial.userName.isNotBlank()
        binding.ivProviderCastcleIcon.setImageResource(
            if (item.syncSocial.provider is SocialType.Facebook) {
                R.drawable.ic_facebook
            } else {
                R.drawable.ic_twitter
            }
        )
        binding.ivProviderCastcleIcon.backgroundTintList = colorStateList(
            if (item.syncSocial.provider is SocialType.Facebook) {
                R.color.blue_facebook
            } else {
                R.color.blue_twitter
            }
        )
        binding.tvAutoPostDescription.text =
            context().getString(R.string.fragment_sync_social_detail_title_3,
                item.syncSocial.provider.id.replaceFirstChar { it.uppercase() })
        binding.tvTitle.text = context().getString(R.string.fragment_sync_social_detail_title_4,
            item.syncSocial.provider.id.replaceFirstChar { it.uppercase() })
        binding.ivAvatarCastcle.loadAvatarImage(item.user.avatar.thumbnail)
        binding.swAutoPost.isChecked = item.syncSocial.autoPost
        binding.tvCastcleName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
    }

}