package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletSendBinding
import com.castcle.android.presentation.wallet.wallet_send.WalletSendListener
import io.reactivex.disposables.CompositeDisposable

class WalletSendViewRenderer : CastcleViewRenderer<WalletSendViewEntity,
    WalletSendViewHolder,
    WalletSendListener>(R.layout.item_wallet_send) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletSendListener,
        compositeDisposable: CompositeDisposable
    ) = WalletSendViewHolder(
        ItemWalletSendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}