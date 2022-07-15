package com.castcle.android.presentation.setting.item_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingMenuBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingMenuViewRenderer : CastcleViewRenderer<SettingMenuViewEntity,
    SettingMenuViewHolder,
    SettingListener>(R.layout.item_setting_menu) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingMenuViewHolder(
        ItemSettingMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}