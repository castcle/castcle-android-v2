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

package com.castcle.android.presentation.feed.item_feed_image

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.CastcleTextView
import com.castcle.android.core.custom_view.LinkedType
import com.castcle.android.core.custom_view.participate_bar.ParticipateBarListener
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedImageBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import com.castcle.android.presentation.feed.item_feed_image_item.FeedImageItemViewEntity
import com.castcle.android.presentation.feed.item_feed_image_item.FeedImageItemViewRenderer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedImageViewHolder(
    private val binding: ItemFeedImageBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
    private val displayType: FeedDisplayType,
) : CastcleViewHolder<FeedImageViewEntity>(binding.root), UserBarListener, ParticipateBarListener,
    FeedImageViewListener {

    override var item = FeedImageViewEntity()

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(FeedImageItemViewRenderer())
        }
    }

    init {
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.castcleTextView.setLinkClickListener(object : CastcleTextView.LinkClickListener {
            override fun onLinkClicked(linkType: LinkedType, matchedText: String) {
                when (linkType) {
                    LinkedType.URL -> listener.onLinkClicked(matchedText)
                    LinkedType.HASHTAG -> listener.onHashtagClicked(matchedText)
                    LinkedType.MENTION -> listener.onMentionClicked(matchedText)
                    else -> Unit
                }
            }
        })
        compositeDisposable += binding.root.onClick {
            if (displayType is FeedDisplayType.QuoteCast) {
                listener.onCommentClicked(item.cast, item.user)
            }
        }
        compositeDisposable += binding.castcleTextView.onClick {
            binding.castcleTextView.toggle()
        }
    }

    override fun bind(bindItem: FeedImageViewEntity) {
        val marginBottom = if (
            displayType is FeedDisplayType.QuoteCast || displayType is FeedDisplayType.Recast
        ) {
            0
        } else {
            dimenPx(com.intuit.sdp.R.dimen._4sdp)
        }
        binding.root.layoutParams = binding.root.layoutParams.cast<RecyclerView.LayoutParams>()
            ?.apply { setMargins(0, 0, 0, marginBottom) }
        if (displayType is FeedDisplayType.NewCast) {
            binding.root.setBackgroundColor(color(R.color.transparent))
            binding.root.background = drawable(R.drawable.bg_outline_corner_16dp)
            binding.root.backgroundTintList = colorStateList(R.color.gray_1)
        } else {
            binding.root.setBackgroundColor(color(R.color.black_background_2))
        }
        binding.participateBar.isGone =
            displayType is FeedDisplayType.QuoteCast || displayType is FeedDisplayType.NewCast
        binding.participateBar.bind(
            adsEnable = item.adsEnable,
            cast = item.cast,
            farmingEnable = item.farmEnable,
            isNotAdPreview = displayType !is FeedDisplayType.AdPreview,
            listener = this,
        )
        binding.reported.root.isVisible = item.cast.reported
        binding.userBar.bind(item.cast, item.user, this, displayType !is FeedDisplayType.NewCast)
        binding.castcleTextView.onClearMessage()
        binding.castcleTextView.isVisible = item.cast.message.isNotBlank()
        if (item.cast.type is CastType.Long) {
            binding.castcleTextView.setCollapseText(item.cast.message)
        } else {
            binding.castcleTextView.appendLinkText(item.cast.message)
        }
        adapter.submitList(item.cast.image.take(4).mapIndexed { index, it ->
            FeedImageItemViewEntity(
                image = it,
                itemCount = item.cast.image.size,
                uniqueId = index.toString(),
            )
        })
        binding.recyclerView.layoutManager = GridLayoutManager(context(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (position) {
                    0, 2 -> if (item.cast.image.size == position + 1) 2 else 1
                    else -> 1
                }
            }
        }
    }

    override fun onCommentClicked(cast: CastEntity) {
        listener.onCommentClicked(cast, item.user)
    }

    override fun onContentFarmingClicked(cast: CastEntity) {
        listener.onContentFarmingClicked(cast)
    }

    override fun onFollowClicked(user: UserEntity) {
        listener.onFollowClicked(user)
    }

    override fun onChildImageClicked(position: Int) {
        listener.onImageClicked(image = item.cast.image, position = position)
    }

    override fun onLikeClicked(cast: CastEntity) {
        listener.onLikeClicked(cast)
    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {
        val optionType = if (cast.isOwner) {
            OptionDialogType.MyContentOption(contentId = cast.id)
        } else {
            OptionDialogType.OtherContentOption(contentId = cast.id)
        }
        listener.onOptionClicked(optionType)
    }

    override fun onRecastClicked(cast: CastEntity) {
        listener.onRecastClicked(cast)
    }

    override fun onUserClicked(user: UserEntity) {
        listener.onUserClicked(user)
    }

    override fun onBoostCastClicked(cast: CastEntity) {
        listener.onBoostCastClicked(cast)
    }
}