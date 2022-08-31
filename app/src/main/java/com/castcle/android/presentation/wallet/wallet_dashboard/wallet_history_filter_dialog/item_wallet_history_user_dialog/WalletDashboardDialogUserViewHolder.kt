package com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.item_wallet_history_user_dialog

import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletDashboardDialogUserBinding
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.wallet.wallet_dashboard.wallet_history_filter_dialog.WalletDashboardDialogListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class WalletDashboardDialogUserViewHolder(
    private val binding: ItemWalletDashboardDialogUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletDashboardDialogListener,
) : CastcleViewHolder<WalletDashboardDialogUserViewEntity>(binding.root) {

    override var item = WalletDashboardDialogUserViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user)
        }
    }

    override fun bind(bindItem: WalletDashboardDialogUserViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvDisplayName.text = item.user.displayName
        binding.ivCheck.isVisible = item.selected
        binding.tvUserType.text = when (item.user.type) {
            is UserType.Page -> string(R.string.page)
            is UserType.People -> string(R.string.profile)
        }
        binding.root.background = if (bindingAdapterPosition == 0) {
            drawable(R.drawable.bg_corner_top_16dp)
        } else {
            drawable(R.drawable.bg_rectangle)
        }
    }

}