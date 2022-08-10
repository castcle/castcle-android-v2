package com.castcle.android.presentation.profile

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.dialog.option.OptionDialogType

interface ProfileListener : CastcleListener {
    fun onFollowClicked(user: UserEntity) = Unit
    fun onFollowingFollowersClicked(isFollowing: Boolean, user: UserEntity) = Unit
    fun onOptionClicked(type: OptionDialogType)
}