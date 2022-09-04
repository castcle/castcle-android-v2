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

package com.castcle.android.presentation.setting.setting.item_profile

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSettingProfileBinding
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingProfileViewHolder(
    private val binding: ItemSettingProfileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingProfileViewEntity>(binding.root) {

    override var item = SettingProfileViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.userWithSocial.user)
        }
    }

    override fun bind(bindItem: SettingProfileViewEntity) {
        val user = item.userWithSocial.user
        val syncSocial = item.userWithSocial.syncSocial.sortedBy { it.provider.id }
        binding.ivAvatar.loadAvatarImage(imageUrl = user.avatar.thumbnail)
        binding.flProvider1.isVisible = syncSocial.isNotEmpty()
        binding.ivProvider1.setImageDrawable(
            if (syncSocial.firstOrNull()?.provider is SocialType.Facebook) {
                drawable(R.drawable.ic_facebook)
            } else {
                drawable(R.drawable.ic_twitter)
            }
        )
        binding.ivProvider1.backgroundTintList =
            if (syncSocial.firstOrNull()?.provider is SocialType.Facebook) {
                colorStateList(R.color.blue_facebook)
            } else {
                colorStateList(R.color.blue_twitter)
            }
        binding.flProvider2.isVisible = syncSocial.size > 1
        binding.ivProvider2.setImageDrawable(
            if (syncSocial.secondOrNull()?.provider is SocialType.Facebook) {
                drawable(R.drawable.ic_facebook)
            } else {
                drawable(R.drawable.ic_twitter)
            }
        )
        binding.ivProvider2.backgroundTintList =
            if (syncSocial.secondOrNull()?.provider is SocialType.Facebook) {
                colorStateList(R.color.blue_facebook)
            } else {
                colorStateList(R.color.blue_twitter)
            }
        binding.root.setPadding(
            start = if (user.type is UserType.People) {
                com.intuit.sdp.R.dimen._6sdp
            } else {
                R.dimen._0sdp
            }
        )
    }

}