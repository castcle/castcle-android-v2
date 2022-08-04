package com.castcle.android.presentation.content.item_reply

import android.content.res.ColorStateList
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemReplyBinding
import com.castcle.android.presentation.content.ContentListener
import com.castcle.android.presentation.content.item_comment.CommentViewEntity
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