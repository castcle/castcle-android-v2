package com.castcle.android.presentation.setting.account.item_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemAccountMenuBinding
import com.castcle.android.presentation.setting.account.AccountListener
import io.reactivex.disposables.CompositeDisposable

class AccountMenuViewRenderer : CastcleViewRenderer<AccountMenuViewEntity,
    AccountMenuViewHolder,
    AccountListener>(R.layout.item_account_menu) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: AccountListener,
        compositeDisposable: CompositeDisposable
    ) = AccountMenuViewHolder(
        ItemAccountMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}