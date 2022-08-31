package com.castcle.android.presentation.wallet.wallet_dashboard.item_wallet_dashboard_empty

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.databinding.ItemWalletDashboardEmptyBinding

class WalletDashboardEmptyViewHolder(
    private val binding: ItemWalletDashboardEmptyBinding,
) : CastcleViewHolder<WalletDashboardEmptyViewEntity>(binding.root) {

    override var item = WalletDashboardEmptyViewEntity()

    override fun bind(bindItem: WalletDashboardEmptyViewEntity) {
        binding.tvDescription.text = context().getString(
            R.string.fragment_wallet_dashboard_title_5,
            item.filter.name,
        )
    }

}