package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemWalletSendShortcutBinding
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewListener
import io.reactivex.disposables.CompositeDisposable

class WalletSendShortcutViewRenderer : CastcleViewRenderer<WalletSendShortcutViewEntity,
    WalletSendShortcutViewHolder,
    WalletSendViewListener>(R.layout.item_wallet_send_shortcut) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: WalletSendViewListener,
        compositeDisposable: CompositeDisposable
    ) = WalletSendShortcutViewHolder(
        ItemWalletSendShortcutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}