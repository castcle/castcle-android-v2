package com.castcle.android.presentation.feed.item_feed_text

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.custom_view.CastcleTextView
import com.castcle.android.core.custom_view.LinkedType
import com.castcle.android.core.custom_view.participate_bar.ParticipateBarListener
import com.castcle.android.core.custom_view.user_bar.UserBarListener
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemFeedTextBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedTextViewHolder(
    private val binding: ItemFeedTextBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
    private val referenceType: CastType?,
) : CastcleViewHolder<FeedTextViewEntity>(binding.root), UserBarListener, ParticipateBarListener {

    override var item = FeedTextViewEntity()

    init {
        compositeDisposable += binding.castcleTextView.onClick {
            binding.castcleTextView.toggle()
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

    override fun bind(bindItem: FeedTextViewEntity) {
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