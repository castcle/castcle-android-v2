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

package com.castcle.android.presentation.setting.sync_social.item_sync_social

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.colorStateList
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSyncSocialBinding
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.presentation.setting.sync_social.SyncSocialListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SyncSocialViewHolder(
    private val binding: ItemSyncSocialBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SyncSocialListener,
) : CastcleViewHolder<SyncSocialViewEntity>(binding.root) {

    override var item = SyncSocialViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            when {
                item.syncSocial != null -> listener.onSyncSocialClicked(item.syncSocial)
                item.type is SocialType.Facebook -> listener.onSyncWithFacebookClicked()
                item.type is SocialType.Twitter -> listener.onSyncWithTwitterClicked()
            }
        }
    }

    override fun bind(bindItem: SyncSocialViewEntity) {
        binding.tvSynced.isVisible = item.syncSocial != null
        binding.tvProvider.text = item.type.id.replaceFirstChar { it.uppercase() }
        binding.ivProvider.setImageResource(
            if (item.type is SocialType.Facebook) {
                R.drawable.ic_facebook
            } else {
                R.drawable.ic_twitter
            }
        )
        binding.ivProvider.backgroundTintList = if (item.type is SocialType.Facebook) {
            colorStateList(R.color.blue_facebook)
        } else {
            colorStateList(R.color.blue_twitter)
        }
    }

}