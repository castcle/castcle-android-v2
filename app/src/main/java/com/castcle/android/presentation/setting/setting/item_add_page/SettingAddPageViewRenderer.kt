package com.castcle.android.presentation.setting.setting.item_add_page

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingAddPageBinding
import com.castcle.android.presentation.setting.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingAddPageViewRenderer : CastcleViewRenderer<SettingAddPageViewEntity,
    SettingAddPageViewHolder,
    SettingListener>(R.layout.item_setting_add_page) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingAddPageViewHolder(
        ItemSettingAddPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}