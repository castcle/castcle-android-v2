package com.castcle.android.presentation.who_to_follow.item_who_to_follow

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWhoToFollowBinding
import com.castcle.android.presentation.who_to_follow.WhoToFollowListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WhoToFollowViewHolder(
    private val binding: ItemWhoToFollowBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WhoToFollowListener,
) : CastcleViewHolder<WhoToFollowViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(item.user)
        }
    }

    override var item = WhoToFollowViewEntity()

    override fun bind(bindItem: WhoToFollowViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvUserName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivOfficial.isVisible = item.user.verifiedOfficial
        binding.tvDescription.text = item.user.overview
        binding.tvDescription.isVisible = !item.user.overview.isNullOrBlank()
        with(binding.tvFollow) {
            isVisible = !item.user.isOwner
            text = if (item.user.followed) {
                string(R.string.followed)
            } else {
                string(R.string.follow)
            }
            background = if (item.user.followed) {
                drawable(R.drawable.bg_corner_16dp)
            } else {
                drawable(R.drawable.bg_outline_corner_16dp)
            }
            setTextColor(
                if (item.user.followed) {
                    color(R.color.white)
                } else {
                    color(R.color.blue)
                }
            )
        }
    }

}