package com.castcle.android.presentation.setting.account.item_menu

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAccountMenuBinding
import com.castcle.android.presentation.setting.account.AccountListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class AccountMenuViewHolder(
    private val binding: ItemAccountMenuBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: AccountListener,
) : CastcleViewHolder<AccountMenuViewEntity>(binding.root) {

    override var item = AccountMenuViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            item.action(listener)
        }
    }

    override fun bind(bindItem: AccountMenuViewEntity) {
        binding.ivArrow.isInvisible = !item.showArrow
        binding.tvTitle.text = string(item.titleId)
        binding.ivImage.setImageResource(item.imageIcon ?: R.drawable.ic_facebook)
        binding.ivImage.backgroundTintList = colorStateList(item.imageColor)
        binding.ivImage.isVisible = item.imageIcon != null
        binding.tvDescription.isVisible = item.description != null
        binding.tvDescription.text = when (val description = item.description) {
            is Int -> string(description)
            is String -> description
            else -> ""
        }
        binding.tvDetail.setTextColor(color(item.detailColor))
        binding.tvDetail.text = when (val detail = item.detail) {
            is Int -> string(detail)
            is String -> detail
            else -> ""
        }
    }

}