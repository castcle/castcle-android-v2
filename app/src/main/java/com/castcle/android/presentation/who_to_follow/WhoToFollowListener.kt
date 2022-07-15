package com.castcle.android.presentation.who_to_follow

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity

interface WhoToFollowListener : CastcleListener {
    fun onFollowClicked(user: UserEntity)
    fun onUserClicked(user: UserEntity)
}