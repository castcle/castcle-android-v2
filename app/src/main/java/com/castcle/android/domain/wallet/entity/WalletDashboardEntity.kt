package com.castcle.android.domain.wallet.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_DASHBOARD
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter

@Entity(tableName = TABLE_WALLET_DASHBOARD)
data class WalletDashboardEntity(
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_createAt", defaultValue = "0")
    val createAt: Long = Long.MAX_VALUE,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_filter", defaultValue = "")
    val filter: WalletHistoryFilter = WalletHistoryFilter.WalletBalance,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_id", defaultValue = "0")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_relationId", defaultValue = "")
    val relationId: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_DASHBOARD}_type", defaultValue = "")
    val type: WalletDashboardType = WalletDashboardType.Balance,
)