package com.castcle.android.domain.wallet.type

import androidx.room.TypeConverter

sealed class WalletDashboardType(val id: String) {

    object Balance : WalletDashboardType(id = "balance")

    object Empty : WalletDashboardType(id = "empty")

    object Error : WalletDashboardType(id = "error")

    object History : WalletDashboardType(id = "history")

    object Loading : WalletDashboardType(id = "loading")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Empty.id -> Empty
            Error.id -> Error
            History.id -> History
            Loading.id -> Loading
            else -> Balance
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: WalletDashboardType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}