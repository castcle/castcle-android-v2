package com.castcle.android.domain.wallet.type

import androidx.room.TypeConverter

sealed class WalletHistoryStatus(val id: String, val name: String) {

    object Failed : WalletHistoryStatus(id = "failed", name = "Failed")

    object Pending : WalletHistoryStatus(id = "pending", name = "Pending")

    object Success : WalletHistoryStatus(id = "verified", name = "Success")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Failed.id -> Failed
            Success.id -> Success
            else -> Pending
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: WalletHistoryStatus): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}