package com.castcle.android.presentation.feed.item_feed_image_3

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.CastcleTextView
import com.castcle.android.core.custom_view.LinkedType
import com.castcle.android.core.custom_view.participate_bar.ParticipateBarListener
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedImage3Binding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.core.entity.ImageEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedImage3ViewHolder(
    private val binding: ItemFeedImage3Binding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
    private val referenceType: CastType?,
) : CastcleViewHolder<FeedImage3ViewEntity>(binding.root), UserBarListener, ParticipateBarListener {

    override var item = FeedImage3ViewEntity()

    init {
        compositeDisposable += binding.castcleTextView.onClick {
            binding.castcleTextView.toggle()
        }
        compositeDisposable += binding.ivImage1.onClick {
            listener.onImageClicked(item.cast.image.getOrNull(0) ?: ImageEntity())
        }
        compositeDisposable += binding.ivImage2.onClick {
            listener.onImageClicked(item.cast.image.getOrNull(1) ?: ImageEntity())
        }
        compositeDisposable += binding.ivImage3.onClick {
            listener.onImageClicked(item.cast.image.getOrNull(2) ?: ImageEntity())
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

    override fun bind(bindItem: FeedImage3ViewEntity) {
        val marginBottom = if (
            referenceType is CastType.Quote || referenceType is CastType.Recast
        ) {
            0
        } else {
            dimenPx(com.intuit.sdp.R.dimen._4sdp)
        }
        binding.root.layoutParams = binding.root.layoutParams.cast<RecyclerView.LayoutParams>()
            ?.apply { setMargins(0, 0, 0, marginBottom) }
        binding.participateBar.isGone = referenceType is CastType.Quote
        binding.participateBar.bind(item.cast, this)
        binding.userBar.bind(item.cast, item.user, this)
        binding.castcleTextView.onClearMessage()
        if (item.cast.type is CastType.Long) {
            binding.castcleTextView.setCollapseText(item.cast.message)
        } else {
            binding.castcleTextView.appendLinkText(item.cast.message)
        }
        binding.ivImage1.loadCenterCropWithRoundedCorners(
            thumbnailUrl = item.cast.image.getOrNull(0)?.thumbnail,
            url = item.cast.image.getOrNull(0)?.original,
            viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2),
            enableTopRight = false,
            enableBottomRight = false,
            enableBottomLeft = false,
        )
        binding.ivImage2.loadCenterCropWithRoundedCorners(
            thumbnailUrl = item.cast.image.getOrNull(1)?.thumbnail,
            url = item.cast.image.getOrNull(1)?.original,
            viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)).div(2),
            enableTopLeft = false,
            enableBottomRight = false,
            enableBottomLeft = false,
        )
        binding.ivImage3.loadScaleCenterCropWithRoundedCorners(
            thumbnailUrl = item.cast.image.getOrNull(2)?.thumbnail,
            url = item.cast.image.getOrNull(2)?.original,
            viewSizeDp = screenWidthPx().minus(dimenPx(com.intuit.sdp.R.dimen._24sdp)),
            enableTopLeft = false,
            enableTopRight = false,
        )
    }

    override fun onCommentClicked(cast: CastEntity) {
        listener.onCommentClicked(cast)
    }

    override fun onFollowClicked(user: UserEntity) {
        listener.onFollowClicked(user)
    }

    override fun onLikeClicked(cast: CastEntity) {
        listener.onLikeClicked(cast)
    }

    override fun onOptionClicked(cast: CastEntity, user: UserEntity) {
        listener.onOptionClicked(cast, user)
    }

    override fun onRecastClicked(cast: CastEntity) {
        listener.onRecastClicked(cast)
    }

    override fun onUserClicked(user: UserEntity) {
        listener.onUserClicked(user)
    }

}