package com.castcle.android.presentation.setting.forgot_password.item_forgot_password

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemForgotPasswordBinding
import com.castcle.android.presentation.setting.forgot_password.ForgotPasswordListener
import io.reactivex.disposables.CompositeDisposable

class ForgotPasswordViewRenderer : CastcleViewRenderer<ForgotPasswordViewEntity,
    ForgotPasswordViewHolder,
    ForgotPasswordListener>(R.layout.item_forgot_password) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: ForgotPasswordListener,
        compositeDisposable: CompositeDisposable
    ) = ForgotPasswordViewHolder(
        ItemForgotPasswordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}