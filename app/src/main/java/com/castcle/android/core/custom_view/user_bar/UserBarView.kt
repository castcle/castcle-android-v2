package com.castcle.android.core.custom_view.user_bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutUserBarBinding
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.type.CastType
import com.castcle.android.domain.user.entity.UserEntity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class UserBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = LayoutUserBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    fun bind(cast: CastEntity, user: UserEntity, listener: UserBarListener) {
        compositeDisposable.clear()
        binding.tvDisplayName.text = user.displayName
        binding.ivAvatar.loadAvatarImage(imageUrl = user.avatar.thumbnail)
        binding.tvFollow.isGone = user.followed || user.isOwner
        binding.ivOfficial.isVisible = user.verifiedOfficial
        when {
            cast.id == "default" -> {
                binding.tvDateTime.text = context.getString(R.string.introduction)
            }
            cast.type is CastType.AdsContent || cast.type is CastType.AdsPage -> {
                binding.tvDateTime.text = context.getString(R.string.advertised)
            }
            else -> {
                binding.tvDateTime.setCustomTimeAgo(cast.createdAt.toTime())
            }
        }
        compositeDisposable += binding.ivOption.onClick {
            listener.onOptionClicked(cast, user)
        }
        compositeDisposable += binding.flAvatar.onClick {
            listener.onUserClicked(user)
        }
        compositeDisposable += binding.tvDisplayName.onClick {
            listener.onUserClicked(user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(user)
        }
    }

}