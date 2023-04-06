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

package com.castcle.android.presentation.notification.item

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemNotificationBinding
import com.castcle.android.domain.notification.type.NotificationType
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.notification.NotificationListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: NotificationListener,
) : CastcleViewHolder<NotificationViewEntity>(binding.root) {

    override var item = NotificationViewEntity()

    init {
        compositeDisposable += binding.ivOption.onClick {
            val option = OptionDialogType.NotificationOption(
                notificationId = item.notification.id,
                isRead = item.notification.isRead,
            )
            listener.onOptionClicked(option)
        }
        compositeDisposable += binding.root.onClick {
            if (item.notification.type is NotificationType.Cast) {
                listener.onCastNotificationClicked(
                    castId = item.notification.actionId,
                    notificationId = item.notification.id,
                )
            } else {
                listener.onFollowNotificationClicked(
                    notificationId = item.notification.id,
                    userId = item.notification.actionId,
                )
            }
        }
    }

    override fun bind(bindItem: NotificationViewEntity) {
        binding.ivAvatar.loadAvatarImage(imageUrl = item.notification.image)
        binding.tvMessage.text = item.notification.message
        binding.tvTimestamp.setCustomTimeAgo(item.notification.updatedAt.toTime())
        binding.root.setBackgroundColor(
            if (item.notification.isRead) {
                color(R.color.black_background_2)
            } else {
                color(R.color.blue_3)
            }
        )
    }

}