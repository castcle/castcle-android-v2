package com.castcle.android.core.custom_view.user_bar

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.user.entity.UserEntity

interface UserBarListener : CastcleListener {
    fun onOptionClicked(cast: CastEntity, user: UserEntity)
    fun onFollowClicked(user: UserEntity)
    fun onUserClicked(user: UserEntity)
}