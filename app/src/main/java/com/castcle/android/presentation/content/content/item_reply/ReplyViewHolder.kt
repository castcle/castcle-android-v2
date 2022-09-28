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

package com.castcle.android.presentation.content.content.item_reply

import android.content.res.ColorStateList
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemReplyBinding
import com.castcle.android.presentation.content.content.ContentListener
import com.castcle.android.presentation.dialog.option.OptionDialogType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.*

class ReplyViewHolder(
    private val binding: ItemReplyBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ContentListener,
) : CastcleViewHolder<ReplyViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.flAvatar.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.tvDisplayName.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.ivLike.onClick {
            listener.onLikeCommentClicked(item.comment)
        }
        compositeDisposable += binding.tvLike.onClick {
            listener.onLikeCommentClicked(item.comment)
        }
        binding.root.setOnLongClickListener {
            if (item.comment.isOwner) {
                listener.onOptionClicked(OptionDialogType.ReplyOption(item.comment.id))
            }
            true
        }
    }

    override var item = ReplyViewEntity()

    override fun bind(bindItem: ReplyViewEntity) {
        binding.viewDivider.isVisible = item.showLine
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvDisplayName.text = item.user.displayName
        binding.tvDateTime.setCustomTimeAgo(Date(item.comment.createdAt))
        binding.tvMessage.text = item.comment.message
        val likeColor = getParticipateColor(item.comment.liked)
        binding.ivLike.imageTintList = ColorStateList.valueOf(likeColor)
        binding.tvLike.text = item.comment.likeCount.toMetrics()
        binding.tvLike.isVisible = item.comment.likeCount > 0
        binding.tvLike.setTextColor(likeColor)
    }

    private fun getParticipateColor(isParticipate: Boolean): Int {
        return if (isParticipate) {
            color(R.color.blue)
        } else {
            color(R.color.white)
        }
    }

    private fun Int.toMetrics(): String {
        return if (this > 0) asComma() else ""
    }

}