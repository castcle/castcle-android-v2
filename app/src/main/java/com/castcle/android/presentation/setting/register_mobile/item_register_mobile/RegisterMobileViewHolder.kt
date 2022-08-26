package com.castcle.android.presentation.setting.register_mobile.item_register_mobile

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemRegisterMobileBinding
import com.castcle.android.presentation.setting.register_mobile.RegisterMobileListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class RegisterMobileViewHolder(
    private val binding: ItemRegisterMobileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: RegisterMobileListener
) : CastcleViewHolder<RegisterMobileViewEntity>(binding.root) {

    override var item = RegisterMobileViewEntity()

    init {
        compositeDisposable += binding.tvCountryCode.onClick {
            listener.onMobileCountryCodeClicked()
        }
    }

}