package com.castcle.android.presentation.feed.item_feed_new_cast

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.loadAvatarImage
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemFeedNewCastBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class FeedNewCastViewHolder(
    private val binding: ItemFeedNewCastBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<FeedNewCastViewEntity>(binding.root) {

    override var item = FeedNewCastViewEntity()

    init {
        compositeDisposable += binding.viewAvatar.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.root.onClick {
            listener.onNewCastClicked(item.user.id)
        }
    }

    override fun bind(bindItem: FeedNewCastViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
    }

}