package com.castcle.android.domain.wallet.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_BALANCE
import com.castcle.android.data.wallet.entity.WalletBalanceResponse

@Entity(tableName = TABLE_WALLET_BALANCE)
data class WalletBalanceEntity(
    @ColumnInfo(name = "${TABLE_WALLET_BALANCE}_adCredit", defaultValue = "")
    val adCredit: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_BALANCE}_availableBalance", defaultValue = "")
    val availableBalance: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_BALANCE}_farmBalance", defaultValue = "")
    val farmBalance: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_BALANCE}_totalBalance", defaultValue = "")
    val totalBalance: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_BALANCE}_userId", defaultValue = "")
    @PrimaryKey
    val userId: String = "",
) {

    companion object {
        fun map(response: WalletBalanceResponse?) = WalletBalanceEntity(
            adCredit = response?.adCredit.orEmpty(),
            availableBalance = response?.availableBalance.orEmpty(),
            farmBalance = response?.farmBalance.orEmpty(),
            totalBalance = response?.totalBalance.orEmpty(),
            userId = response?.id.orEmpty(),
        )
    }

}