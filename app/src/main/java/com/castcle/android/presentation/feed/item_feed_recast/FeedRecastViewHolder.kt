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

package com.castcle.android.presentation.feed.item_feed_recast

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemFeedRecastBinding
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image.FeedImageViewRenderer
import com.castcle.android.presentation.feed.item_feed_text.FeedTextViewRenderer
import com.castcle.android.presentation.feed.item_feed_web.FeedWebViewRenderer
import com.castcle.android.presentation.feed.item_feed_web_image.FeedWebImageViewRenderer
import io.reactivex.disposables.CompositeDisposable

class FeedRecastViewHolder(
    private val binding: ItemFeedRecastBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedRecastViewEntity>(binding.root) {

    override var item = FeedRecastViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(FeedImageViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedTextViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedWebViewRenderer(FeedDisplayType.Recast))
            registerRenderer(FeedWebImageViewRenderer(FeedDisplayType.Recast))
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun bind(bindItem: FeedRecastViewEntity) {
        adapter.submitList(item.reference)
        binding.tvRecast.text = if (item.user.isOwner && item.user.type is UserType.People) {
            string(R.string.you_recasted)
        } else {
            context().getString(R.string.user_recasted, item.user.displayName)
        }
    }

}