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

package com.castcle.android.presentation.feed

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.option.OptionDialogType

interface FeedListener : CastcleListener {
    fun onCommentClicked(cast: CastEntity, user: UserEntity) = Unit
    fun onContentFarmingClicked(cast: CastEntity) = Unit
    fun onFollowClicked(user: UserEntity) = Unit
    fun onHashtagClicked(keyword: String) = Unit
    fun onImageClicked(image: List<ImageEntity>, position: Int) = Unit
    fun onLikeClicked(cast: CastEntity) = Unit
    fun onLinkClicked(url: String) = Unit
    fun onMentionClicked(castcleId: String) = Unit
    fun onNewCastClicked(userId: String) = Unit
    fun onOptionClicked(type: OptionDialogType) = Unit
    fun onRecastClicked(cast: CastEntity) = Unit
    fun onUserClicked(user: UserEntity) = Unit
    fun onViewReportClicked(id: String, ignoreReportContentId: List<String>) = Unit
    fun onWhoToFollowClicked() = Unit
    fun onBoostCastClicked(cast: CastEntity) = Unit
}