package com.castcle.android.presentation.setting.account.item_menu

import androidx.core.view.isInvisible
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.string
import com.castcle.android.databinding.ItemAccountMenuBinding
import com.castcle.android.presentation.setting.account.AccountListener
import io.reactivex.disposables.CompositeDisposable

class AccountMenuViewHolder(
    private val binding: ItemAccountMenuBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: AccountListener,
) : CastcleViewHolder<AccountMenuViewEntity>(binding.root) {

    override var item = AccountMenuViewEntity()

    override fun bind(bindItem: AccountMenuViewEntity) {
        binding.ivArrow.isInvisible = !item.showArrow
        binding.tvTitle.text = string(item.titleId)
        binding.tvDescription.text = if (item.description is Int) {
            string(item.description.cast<Int>())
        } else {
            "${item.description}"
        }
    }

}