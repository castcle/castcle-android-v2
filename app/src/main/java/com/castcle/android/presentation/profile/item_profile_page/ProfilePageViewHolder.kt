package com.castcle.android.presentation.profile.item_profile_page

import android.annotation.SuppressLint
import androidx.core.view.isGone
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemProfilePageBinding
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.profile.ProfileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class ProfilePageViewHolder(
    private val binding: ItemProfilePageBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ProfileListener,

    ) : CastcleViewHolder<ProfilePageViewEntity>(binding.root) {

    override var item = ProfilePageViewEntity()

    init {
        compositeDisposable += binding.ivOption.onClick {
            val type = if (item.user.isOwner) {
                OptionDialogType.MyPageOption(userId = item.user.id)
            } else {
                OptionDialogType.OtherUserOption(userId = item.user.id)
            }
            listener.onOptionClicked(type)
        }
        compositeDisposable += binding.ivAvatar.onClick {

        }
        compositeDisposable += binding.viewFollowers.onClick {
            listener.onFollowingFollowersClicked(isFollowing = false, user = item.user)
        }
        compositeDisposable += binding.tvViewProfile.onClick {

        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(user = item.user)
        }
    }

    override fun bind(bindItem: ProfilePageViewEntity) {
        binding.tvFollower.text = "${item.user.followers.asCount()}\n${string(R.string.followers)}"
        binding.tvCasts.text = "${item.user.casts.asCount()}\n${string(R.string.casts)}"
        binding.tvOverview.isGone = item.user.overview.isNullOrBlank()
        binding.tvOverview.text = item.user.overview?.trim()
        binding.tvDisplayName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.ivCover.loadScaleCenterCrop(
            scale = 12 to 10,
            thumbnailUrl = item.user.cover?.thumbnail,
            url = item.user.cover?.original,
        )
        binding.tvViewProfile.text = if (item.user.isOwner) {
            string(R.string.edit_page)
        } else {
            string(R.string.view_page)
        }
        with(binding.tvFollow) {
            isGone = item.user.isOwner
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