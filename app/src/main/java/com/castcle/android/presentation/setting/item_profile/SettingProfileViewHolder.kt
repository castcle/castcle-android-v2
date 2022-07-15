package com.castcle.android.presentation.setting.item_profile

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSettingProfileBinding
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingProfileViewHolder(
    private val binding: ItemSettingProfileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingProfileViewEntity>(binding.root) {

    override var item = SettingProfileViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.userWithSocial.user)
        }
    }

    override fun bind(bindItem: SettingProfileViewEntity) {
        val user = item.userWithSocial.user
        val syncSocial = item.userWithSocial.syncSocial.sortedBy { it.provider.id }
        binding.ivAvatar.loadAvatarImage(imageUrl = user.avatar.thumbnail)
        binding.flProvider1.isVisible = syncSocial.isNotEmpty()
        binding.ivProvider1.setImageDrawable(
            if (syncSocial.firstOrNull()?.provider is SocialType.Facebook) {
                drawable(R.drawable.ic_facebook)
            } else {
                drawable(R.drawable.ic_twitter)
            }
        )
        binding.ivProvider1.backgroundTintList =
            if (syncSocial.firstOrNull()?.provider is SocialType.Facebook) {
                colorStateList(R.color.blue_facebook)
            } else {
                colorStateList(R.color.blue_twitter)
            }
        binding.flProvider2.isVisible = syncSocial.size > 1
        binding.ivProvider2.setImageDrawable(
            if (syncSocial.secondOrNull()?.provider is SocialType.Facebook) {
                drawable(R.drawable.ic_facebook)
            } else {
                drawable(R.drawable.ic_twitter)
            }
        )
        binding.ivProvider2.backgroundTintList =
            if (syncSocial.secondOrNull()?.provider is SocialType.Facebook) {
                colorStateList(R.color.blue_facebook)
            } else {
                colorStateList(R.color.blue_twitter)
            }
        binding.root.setPadding(
            start = if (user.type is UserType.People) {
                com.intuit.sdp.R.dimen._6sdp
            } else {
                R.dimen._0sdp
            }
        )
    }

}