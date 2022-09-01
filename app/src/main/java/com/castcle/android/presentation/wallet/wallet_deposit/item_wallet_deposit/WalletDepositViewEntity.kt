package com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.extensions.cast

data class WalletDepositViewEntity(
    val castcleId: String = "",
    val qrCode: String = "",
    val userId: String = "",
    override val uniqueId: String = "${R.layout.item_wallet_deposit}",
) : CastcleViewEntity {

    override fun sameAs(isSameItem: Boolean, target: Any?) = if (isSameItem) {
        target?.cast<WalletDepositViewEntity>()?.uniqueId == uniqueId
    } else {
        target?.cast<WalletDepositViewEntity>() == this
    }

    override fun viewType() = R.layout.item_wallet_deposit

}