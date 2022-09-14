package com.castcle.android.presentation.wallet.wallet_address

import com.castcle.android.core.base.recyclerview.CastcleListener
import com.castcle.android.domain.user.entity.UserEntity

interface WalletAddressListener : CastcleListener {
    fun onUserClicked(user: UserEntity)
}