package com.castcle.android.domain.wallet.type

import androidx.room.TypeConverter

sealed class WalletHistoryType(val id: String, val name: String) {

    object Airdrop : WalletHistoryType(id = "airdrop", name = "Airdrop Reward")

    object Deposit : WalletHistoryType(id = "deposit", name = "Deposit")

    object ContentReach : WalletHistoryType(id = "content-reach", name = "Content Reach")

    object Farmed : WalletHistoryType(id = "farmed", name = "Farmed")

    object Farming : WalletHistoryType(id = "farming", name = "Farming")

    object Receive : WalletHistoryType(id = "receive", name = "Receive")

    object Referral : WalletHistoryType(id = "referral", name = "Referral Reward")

    object SeenAds : WalletHistoryType(id = "seen-ads", name = "Seen Ads")

    object Send : WalletHistoryType(id = "send", name = "Send")

    object Social : WalletHistoryType(id = "social", name = "Social Reward")

    object Unfarming : WalletHistoryType(id = "unfarming", name = "Unfarming")

    object Withdraw : WalletHistoryType(id = "withdraw", name = "Withdraw")

    companion object {
        fun getFromId(id: String?) = when (id) {
            Airdrop.id -> Airdrop
            Deposit.id -> Deposit
            ContentReach.id -> ContentReach
            Farmed.id -> Farmed
            Farming.id -> Farming
            Receive.id -> Receive
            Referral.id -> Referral
            SeenAds.id -> SeenAds
            Send.id -> Send
            Social.id -> Social
            Unfarming.id -> Unfarming
            else -> Withdraw
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: WalletHistoryType): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}