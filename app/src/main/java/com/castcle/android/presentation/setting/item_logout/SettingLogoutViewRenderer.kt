package com.castcle.android.presentation.setting.item_logout

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingLogoutBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingLogoutViewRenderer : CastcleViewRenderer<SettingLogoutViewEntity,
    SettingLogoutViewHolder,
    SettingListener>(R.layout.item_setting_logout) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingLogoutViewHolder(
        ItemSettingLogoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}