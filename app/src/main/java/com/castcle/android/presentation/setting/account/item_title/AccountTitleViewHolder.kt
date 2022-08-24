package com.castcle.android.presentation.setting.account.item_title

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemAccountTitleBinding

class AccountTitleViewHolder(
    private val binding: ItemAccountTitleBinding,
) : CastcleViewHolder<AccountTitleViewEntity>(binding.root) {

    override var item = AccountTitleViewEntity()

    override fun bind(bindItem: AccountTitleViewEntity) {
        binding.root.text = string(item.titleId)
    }

}