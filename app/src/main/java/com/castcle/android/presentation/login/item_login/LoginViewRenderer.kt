package com.castcle.android.presentation.login.item_login

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemLoginBinding
import com.castcle.android.presentation.login.LoginListener
import io.reactivex.disposables.CompositeDisposable

class LoginViewRenderer : CastcleViewRenderer<LoginViewEntity,
    LoginViewHolder,
    LoginListener>(R.layout.item_login) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: LoginListener,
        compositeDisposable: CompositeDisposable,
    ) = LoginViewHolder(
        ItemLoginBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}