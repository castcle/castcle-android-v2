package com.castcle.android.presentation.setting.account.item_title

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemAccountTitleBinding
import com.castcle.android.presentation.setting.account.AccountListener
import io.reactivex.disposables.CompositeDisposable

class AccountTitleViewRenderer : CastcleViewRenderer<AccountTitleViewEntity,
    AccountTitleViewHolder,
    AccountListener>(R.layout.item_account_title) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: AccountListener,
        compositeDisposable: CompositeDisposable
    ) = AccountTitleViewHolder(
        ItemAccountTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

}