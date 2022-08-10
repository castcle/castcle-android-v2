package com.castcle.android.presentation.profile.item_profile_user

import android.annotation.SuppressLint
import androidx.core.view.isGone
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemProfileUserBinding
import com.castcle.android.presentation.dialog.option.OptionDialogType
import com.castcle.android.presentation.profile.ProfileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@SuppressLint("SetTextI18n")
class ProfileUserViewHolder(
    private val binding: ItemProfileUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ProfileListener,

    ) : CastcleViewHolder<ProfileUserViewEntity>(binding.root) {

    override var item = ProfileUserViewEntity()

    init {
        compositeDisposable += binding.ivOption.onClick {
            val type = if (item.user.isOwner) {
                OptionDialogType.MyUserOption(userId = item.user.id)
            } else {
                OptionDialogType.OtherUserOption(userId = item.user.id)
            }
            listener.onOptionClicked(type)
        }
        compositeDisposable += binding.ivAvatar.onClick {

        }
        compositeDisposable += binding.viewFollowing.onClick {
            listener.onFollowingFollowersClicked(isFollowing = true, user = item.user)
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

    override fun bind(bindItem: ProfileUserViewEntity) {
        binding.tvFollower.text = "${item.user.followers.asCount()} ${string(R.string.followers)}"
        binding.tvFollowing.text = "${item.user.following.asCount()} ${string(R.string.following)}"
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
            string(R.string.edit_profile)
        } else {
            string(R.string.view_profile)
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