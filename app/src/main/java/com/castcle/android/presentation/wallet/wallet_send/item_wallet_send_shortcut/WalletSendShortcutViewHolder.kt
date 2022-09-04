package com.castcle.android.presentation.wallet.wallet_send.item_wallet_send_shortcut

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletSendShortcutBinding
import com.castcle.android.presentation.wallet.wallet_send.item_wallet_send.WalletSendViewListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletSendShortcutViewHolder(
    private val binding: ItemWalletSendShortcutBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletSendViewListener,
) : CastcleViewHolder<WalletSendShortcutViewEntity>(binding.root) {

    override var item = WalletSendShortcutViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onShortcutClicked(item.user.castcleId, item.user.id)
        }
    }

    override fun bind(bindItem: WalletSendShortcutViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvDisplayName.text = item.user.castcleId
        binding.root.setPadding(
            start = if (bindingAdapterPosition == 0) {
                com.intuit.sdp.R.dimen._6sdp
            } else {
                R.dimen._0sdp
            }
        )
    }

}