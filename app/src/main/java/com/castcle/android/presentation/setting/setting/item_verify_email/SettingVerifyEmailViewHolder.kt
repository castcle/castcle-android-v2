package com.castcle.android.presentation.setting.setting.item_verify_email

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSettingVerifyEmailBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingVerifyEmailViewHolder(
    binding: ItemSettingVerifyEmailBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener
) : CastcleViewHolder<SettingVerifyEmailViewEntity>(binding.root) {

    override var item = SettingVerifyEmailViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            listener.onResentVerifyEmailClicked()
        }
    }

}