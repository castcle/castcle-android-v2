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

package com.castcle.android.core.custom_view.participate_bar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.asCount
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.LayoutParticipateBarBinding
import com.castcle.android.domain.cast.entity.CastEntity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ParticipateBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding by lazy {
        LayoutParticipateBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val compositeDisposable = CompositeDisposable()

    fun bind(cast: CastEntity, listener: ParticipateBarListener) {
        compositeDisposable.clear()
        compositeDisposable += binding.clLike.onClick {
            listener.onLikeClicked(cast)
        }
        compositeDisposable += binding.clComment.onClick {
            listener.onCommentClicked(cast)
        }
        compositeDisposable += binding.clRecast.onClick {
            listener.onRecastClicked(cast)
        }
        compositeDisposable += binding.clContentFarming.onClick {
            listener.onContentFarmingClicked(cast)
        }

        val likeColor = getParticipateColor(cast.liked)
        binding.ivLike.imageTintList = ColorStateList.valueOf(likeColor)
        binding.tvLike.text = cast.likeCount.asCount()
        binding.tvLike.isVisible = cast.likeCount > 0
        binding.tvLike.setTextColor(likeColor)

        val commentColor = getParticipateColor(cast.commented)
        binding.ivComment.imageTintList = ColorStateList.valueOf(commentColor)
        binding.tvComment.text = cast.commentCount.asCount()
        binding.tvComment.isVisible = cast.commentCount > 0
        binding.tvComment.setTextColor(commentColor)

        val recastColor = getParticipateColor(cast.recasted)
        binding.ivRecast.imageTintList = ColorStateList.valueOf(recastColor)
        binding.tvRecast.text = cast.recastCount.asCount()
        binding.tvRecast.isVisible = cast.recastCount > 0
        binding.tvRecast.setTextColor(recastColor)

        val contentFarmingColor = getParticipateColor(cast.farming)
        binding.ivContentFarming.imageTintList = ColorStateList.valueOf(contentFarmingColor)
        binding.tvContentFarming.text = cast.farmCount.asCount()
        binding.tvContentFarming.isVisible = cast.farmCount > 0
        binding.tvContentFarming.setTextColor(contentFarmingColor)
    }

    private fun getParticipateColor(isParticipate: Boolean): Int {
        return if (isParticipate) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.white)
        }
    }

}