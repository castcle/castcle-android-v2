package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_add_shortcut

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemWalletSendAddShortcutBinding
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletSendAddShortcutViewHolder(
    binding: ItemWalletSendAddShortcutBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletSendViewListener,
) : CastcleViewHolder<WalletSendAddShortcutViewEntity>(binding.root) {

    override var item = WalletSendAddShortcutViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onAddShortcutClicked()
        }
    }

}