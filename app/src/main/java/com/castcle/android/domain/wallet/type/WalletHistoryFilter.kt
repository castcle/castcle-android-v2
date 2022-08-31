package com.castcle.android.domain.wallet.type

import android.os.Parcelable
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize

sealed class WalletHistoryFilter(val id: String, val name: String) : Parcelable {

    @Parcelize
    object AirdropReferral : WalletHistoryFilter(id = "airdrop-referral", "Referral & Airdrop")

    @Parcelize
    object ContentFarming : WalletHistoryFilter(id = "content-farming", "Content Farming")

    @Parcelize
    object DepositSend : WalletHistoryFilter(id = "deposit-send", "Deposit & Send")

    @Parcelize
    object SocialRewards : WalletHistoryFilter(id = "social-rewards", "Social Rewards")

    @Parcelize
    object WalletBalance : WalletHistoryFilter(id = "wallet-balance", "Wallet Balance")

    companion object {
        fun getFromId(id: String?) = when (id) {
            AirdropReferral.id -> AirdropReferral
            ContentFarming.id -> ContentFarming
            DepositSend.id -> DepositSend
            SocialRewards.id -> SocialRewards
            else -> WalletBalance
        }
    }

    class Converter {

        @TypeConverter
        fun fromEntity(item: WalletHistoryFilter): String = item.id

        @TypeConverter
        fun toEntity(item: String) = getFromId(item)

    }

}