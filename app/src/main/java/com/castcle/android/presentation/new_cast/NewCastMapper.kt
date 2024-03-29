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

package com.castcle.android.presentation.new_cast

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewEntity
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewEntity
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewEntity
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewEntity
import org.koin.core.annotation.Factory

@Factory
class NewCastMapper {

    fun apply(item: CastWithUserEntity): CastcleViewEntity {
        return when {
            item.cast.image.isNotEmpty() -> FeedImageViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            item.cast.linkPreview.isNotBlank() -> FeedWebImageViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            item.cast.linkUrl.isNotBlank() -> FeedWebViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
            else -> FeedTextViewEntity(
                cast = item.cast,
                uniqueId = item.cast.id,
                user = item.user ?: UserEntity(),
            )
        }
    }

}