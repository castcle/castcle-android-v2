package com.castcle.android.presentation.profile

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity

interface ProfileListener : CastcleListener {
    fun onFollowClicked(user: UserEntity) = Unit
}