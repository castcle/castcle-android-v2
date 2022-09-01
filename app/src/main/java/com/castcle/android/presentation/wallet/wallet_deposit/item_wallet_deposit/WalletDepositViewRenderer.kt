package com.castcle.android.presentation.wallet.wallet_deposit.item_wallet_deposit

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletDepositBinding
import com.castcle.android.presentation.wallet.wallet_deposit.WalletDepositListener
import io.reactivex.disposables.CompositeDisposable

class WalletDepositViewRenderer : CastcleViewRenderer<WalletDepositViewEntity,
    WalletDepositViewHolder,
    WalletDepositListener>(R.layout.item_wallet_deposit) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletDepositListener,
        compositeDisposable: CompositeDisposable
    ) = WalletDepositViewHolder(
        ItemWalletDepositBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}