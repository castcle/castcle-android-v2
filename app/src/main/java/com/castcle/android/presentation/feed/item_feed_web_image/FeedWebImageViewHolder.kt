package com.castcle.android.presentation.feed.item_feed_web_image

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.CastcleTextView
import com.castcle.android.core.custom_view.LinkedType
import com.castcle.android.core.custom_view.participate_bar.ParticipateBarListener
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedWebImageBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.FeedDisplayType
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedWebImageViewHolder(
    private val binding: ItemFeedWebImageBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
    private val displayType: FeedDisplayType,
) : CastcleViewHolder<FeedWebImageViewEntity>(binding.root), UserBarListener,
    ParticipateBarListener {

    override var item = FeedWebImageViewEntity()

    init {
        compositeDisposable += binding.castcleTextView.onClick {
            binding.castcleTextView.toggle()
        }
        compositeDisposable += binding.clWeb.onClick {
            listener.onLinkClicked(item.cast.linkUrl)
        }
        binding.castcleTextView.setLinkClickListener(object : CastcleTextView.LinkClickListener {
            override fun onLinkClicked(linkType: LinkedType, matchedText: String) {
                if (linkType == LinkedType.URL) {
                    listener.onLinkClicked(matchedText)
                } else if (linkType == LinkedType.HASHTAG) {
                    listener.onHashtagClicked(matchedText)
                }
            }
        })
    }

    override fun bind(bindItem: FeedWebImageViewEntity) {
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
        binding.participateBar.bind(item.cast, this)
        binding.reported.root.isVisible = item.cast.reported
        binding.userBar.bind(item.cast, item.user, this, displayType !is FeedDisplayType.NewCast)
        binding.castcleTextView.onClearMessage()
        if (item.cast.type is CastType.Long) {
            binding.castcleTextView.setCollapseText(item.cast.message)
        } else {
            binding.castcleTextView.appendLinkText(item.cast.message)
        }
        binding.tvTitle.isVisible = item.cast.linkTitle.isNotBlank()
        binding.tvTitle.text = item.cast.linkTitle
        binding.tvDescription.text = item.cast.linkDescription.ifBlank { item.cast.message }
        binding.ivImage.loadScaleCenterCropWithRoundedCorners(
            cornersSizeId = com.intuit.sdp.R.dimen._15sdp,
            thumbnailUrl = item.cast.linkPreview,
            url = item.cast.linkPreview,
            scaleHeight = 9,
            scaleWidth = 16,
            viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)),
            enableBottomLeft = false,
            enableBottomRight = false,
        )
    }

    override fun onCommentClicked(cast: CastEntity) {
        listener.onCommentClicked(cast, item.user)
    }

    override fun onFollowClicked(user: UserEntity) {
        listener.onFollowClicked(user)
    }

    override fun onLikeClicked(cast: CastEntity) {
        listener.onLikeClicked(cast)
    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {
        listener.onContentOptionClicked(cast, user)
    }

    override fun onRecastClicked(cast: CastEntity) {
        listener.onRecastClicked(cast)
    }

    override fun onUserClicked(user: UserEntity) {
        listener.onUserClicked(user)
    }

}