package com.castcle.android.presentation.setting.setting.item_verify_email

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingVerifyEmailBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingVerifyEmailViewRenderer : CastcleViewRenderer<SettingVerifyEmailViewEntity,
    SettingVerifyEmailViewHolder,
    SettingListener>(R.layout.item_setting_verify_email) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingVerifyEmailViewHolder(
        ItemSettingVerifyEmailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}