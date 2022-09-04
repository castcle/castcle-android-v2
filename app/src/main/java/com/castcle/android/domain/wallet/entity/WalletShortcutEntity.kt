package com.castcle.android.domain.wallet.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_SHORT_CUT

@Entity(tableName = TABLE_WALLET_SHORT_CUT)
data class WalletShortcutEntity(
    @ColumnInfo(name = "${TABLE_WALLET_SHORT_CUT}_id", defaultValue = "")
    @PrimaryKey
    val id: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_SHORT_CUT}_order", defaultValue = "0")
    val order: Int = 0,
    @ColumnInfo(name = "${TABLE_WALLET_SHORT_CUT}_userId", defaultValue = "")
    val userId: String = "",
)