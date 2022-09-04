package com.castcle.android.domain.wallet.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_USER
import com.castcle.android.core.constants.TABLE_WALLET_SHORT_CUT
import com.castcle.android.domain.user.entity.UserEntity

data class WalletShortcutWithResultEntity(
    @Embedded
    val shortcut: WalletShortcutEntity = WalletShortcutEntity(),
    @Relation(parentColumn = "${TABLE_WALLET_SHORT_CUT}_userId", entityColumn = "${TABLE_USER}_id")
    val user: UserEntity? = null,
)