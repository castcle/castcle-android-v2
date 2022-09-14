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

package com.castcle.android.core.custom_view.user_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutUserBarBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class UserBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = LayoutUserBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    fun bind(cast: CastEntity, user: UserEntity, listener: UserBarListener, optionEnable: Boolean) {
        compositeDisposable.clear()
        binding.ivOption.isVisible = optionEnable
        binding.tvDisplayName.text = user.displayName
        binding.ivAvatar.loadAvatarImage(imageUrl = user.avatar.thumbnail)
        binding.tvFollow.isGone = user.followed || user.isOwner
        binding.ivOfficial.isVisible = user.verifiedOfficial == true
        when {
            cast.id == "default" -> {
                binding.tvDateTime.text = context.getString(R.string.introduction)
            }
            cast.type is CastType.AdsContent || cast.type is CastType.AdsPage -> {
                binding.tvDateTime.text = context.getString(R.string.advertised)
            }
            else -> {
                binding.tvDateTime.setCustomTimeAgo(cast.createdAt.toTime())
            }
        }
        compositeDisposable += binding.ivOption.onClick {
            listener.onOptionClicked(cast, user)
        }
        compositeDisposable += binding.flAvatar.onClick {
            listener.onUserClicked(user)
        }
        compositeDisposable += binding.tvDisplayName.onClick {
            listener.onUserClicked(user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(user)
        }
    }

}