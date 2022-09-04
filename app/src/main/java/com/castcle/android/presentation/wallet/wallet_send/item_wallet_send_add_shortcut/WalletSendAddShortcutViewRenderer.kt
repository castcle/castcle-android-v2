package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_add_shortcut

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletSendAddShortcutBinding
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewListener
import io.reactivex.disposables.CompositeDisposable

class WalletSendAddShortcutViewRenderer : CastcleViewRenderer<WalletSendAddShortcutViewEntity,
    WalletSendAddShortcutViewHolder,
    WalletSendViewListener>(R.layout.item_wallet_send_add_shortcut) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletSendViewListener,
        compositeDisposable: CompositeDisposable
    ) = WalletSendAddShortcutViewHolder(
        ItemWalletSendAddShortcutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}