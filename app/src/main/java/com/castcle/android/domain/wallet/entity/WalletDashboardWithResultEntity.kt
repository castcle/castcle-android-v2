package com.castcle.android.domain.wallet.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.*
import com.castcle.android.domain.user.entity.UserEntity

data class WalletDashboardWithResultEntity(
    @Relation(
        parentColumn = "${TABLE_WALLET_DASHBOARD}_relationId",
        entityColumn = "${TABLE_WALLET_BALANCE}_userId",
    )
    val balance: WalletBalanceEntity? = null,
    @Embedded
    val dashboard: WalletDashboardEntity = WalletDashboardEntity(),
    @Relation(
        parentColumn = "${TABLE_WALLET_DASHBOARD}_relationId",
        entityColumn = "${TABLE_WALLET_HISTORY}_id",
    )
    val history: WalletHistoryEntity? = null,
    @Relation(
        parentColumn = "${TABLE_WALLET_DASHBOARD}_relationId",
        entityColumn = "${TABLE_USER}_id",
    )
    val user: UserEntity? = null,
)