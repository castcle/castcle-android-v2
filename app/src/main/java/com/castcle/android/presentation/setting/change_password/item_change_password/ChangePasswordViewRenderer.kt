package com.castcle.android.presentation.setting.change_password.item_change_password

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemChangePasswordBinding
import com.castcle.android.presentation.setting.change_password.ChangePasswordListener
import io.reactivex.disposables.CompositeDisposable

class ChangePasswordViewRenderer : CastcleViewRenderer<ChangePasswordViewEntity,
    ChangePasswordViewHolder,
    ChangePasswordListener>(R.layout.item_change_password) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ChangePasswordListener,
        compositeDisposable: CompositeDisposable
    ) = ChangePasswordViewHolder(
        ItemChangePasswordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}