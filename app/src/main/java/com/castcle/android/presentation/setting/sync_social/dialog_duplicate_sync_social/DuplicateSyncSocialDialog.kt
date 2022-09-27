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

package com.castcle.android.presentation.setting.sync_social.dialog_duplicate_sync_social

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.dialog.BaseDialog
import com.castcle.android.core.extensions.loadAvatarImage
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.DialogDuplicateSyncSocialBinding
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.SocialType
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class DuplicateSyncSocialDialog(
    private val binding: DialogDuplicateSyncSocialBinding,
    private val syncSocial: SyncSocialEntity,
    private val user: UserEntity,
) : BaseDialog(binding = binding, width = 0.8) {

    override fun initViewProperties() {
        binding.ivAvatarProvider.loadAvatarImage(syncSocial.avatar)
        binding.tvProviderName.text = syncSocial.displayName
        binding.tvProviderId.text = "@${syncSocial.userName}"
        binding.tvProviderId.isVisible = syncSocial.userName.isNotBlank()
        binding.ivProviderCastcleIcon.setImageResource(
            if (syncSocial.provider is SocialType.Facebook) {
                R.drawable.ic_facebook
            } else {
                R.drawable.ic_twitter
            }
        )
        binding.ivProviderCastcleIcon.backgroundTintList = ColorStateList.valueOf(
            if (syncSocial.provider is SocialType.Facebook) {
                context.getColor(R.color.blue_facebook)
            } else {
                context.getColor(R.color.blue_twitter)
            }
        )
        binding.ivAvatarCastcle.loadAvatarImage(user.avatar.thumbnail)
        binding.tvCastcleName.text = user.displayName
        binding.tvCastcleId.text = user.castcleId
        binding.tvDescription.text = context.getString(
            R.string.fragment_sync_social_detail_title_8,
            syncSocial.provider.id.replaceFirstChar { it.uppercase() },
            syncSocial.userName,
            user.castcleId,
            user.castcleId,
        )
    }

    override fun initListener() {
        compositeDisposable += binding.tvAccept.onClick {
            dismiss()
        }
    }

}