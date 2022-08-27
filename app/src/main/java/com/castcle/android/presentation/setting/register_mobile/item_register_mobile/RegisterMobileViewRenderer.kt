package com.castcle.android.presentation.setting.register_mobile.item_register_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemRegisterMobileBinding
import com.castcle.android.presentation.setting.register_mobile.RegisterMobileListener
import io.reactivex.disposables.CompositeDisposable

class RegisterMobileViewRenderer : CastcleViewRenderer<RegisterMobileViewEntity,
    RegisterMobileViewHolder,
    RegisterMobileListener>(R.layout.item_register_mobile) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: RegisterMobileListener,
        compositeDisposable: CompositeDisposable
    ) = RegisterMobileViewHolder(
        ItemRegisterMobileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}