package com.castcle.android.domain.wallet.entity

import androidx.room.*
import com.castcle.android.core.base.response.BaseResponse
import com.castcle.android.core.constants.TABLE_WALLET_HISTORY
import com.castcle.android.core.extensions.toMilliSecond
import com.castcle.android.data.wallet.entity.WalletHistoryResponse
import com.castcle.android.domain.wallet.type.WalletHistoryStatus
import com.castcle.android.domain.wallet.type.WalletHistoryType

@Entity(tableName = TABLE_WALLET_HISTORY)
data class WalletHistoryEntity(
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_createdAt", defaultValue = "0")
    val createdAt: Long = 0,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_id", defaultValue = "")
    @PrimaryKey
    val id: String = "",
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_status", defaultValue = "")
    val status: WalletHistoryStatus = WalletHistoryStatus.Pending,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_type", defaultValue = "")
    val type: WalletHistoryType = WalletHistoryType.Send,
    @ColumnInfo(name = "${TABLE_WALLET_HISTORY}_value", defaultValue = "0")
    val value: Double = 0.0,
) {

    companion object {
        fun map(response: BaseResponse<List<WalletHistoryResponse>>?) =
            response?.payload.orEmpty().map {
                WalletHistoryEntity(
                    createdAt = it.createdAt?.toMilliSecond() ?: 0,
                    id = it.id.orEmpty(),
                    status = WalletHistoryStatus.getFromId(it.status),
                    type = WalletHistoryType.getFromId(it.type),
                    value = it.value ?: 0.0,
                )
            }
    }

}