package com.castcle.android.presentation.setting.setting.item_notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingNotificationBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingNotificationViewRenderer : CastcleViewRenderer<SettingNotificationViewEntity,
    SettingNotificationViewHolder,
    SettingListener>(R.layout.item_setting_notification) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingNotificationViewHolder(
        ItemSettingNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}