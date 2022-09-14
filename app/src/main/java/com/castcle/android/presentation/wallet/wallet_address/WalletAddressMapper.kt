package com.castcle.android.presentation.wallet.wallet_address

import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.presentation.wallet.wallet_address.item_empty_wallet_address.EmptyWalletAddressViewEntity
import com.castcle.android.presentation.wallet.wallet_address.item_recent_wallet_address.RecentWalletAddressViewEntity
import com.castcle.android.presentation.wallet.wallet_address.item_wallet_address.WalletAddressViewEntity
import org.koin.core.annotation.Factory

@Factory
class WalletAddressMapper {

    fun map(keyword: String, user: List<UserEntity>): List<CastcleViewEntity> {
        val items = user.map { WalletAddressViewEntity(uniqueId = it.id, user = it) }
        return when {
            items.isEmpty() -> listOf(EmptyWalletAddressViewEntity())
            keyword.isBlank() -> listOf(RecentWalletAddressViewEntity()).plus(items)
            else -> items
        }
    }

}