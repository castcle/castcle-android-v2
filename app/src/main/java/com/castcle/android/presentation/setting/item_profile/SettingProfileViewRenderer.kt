package com.castcle.android.presentation.setting.item_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingProfileBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingProfileViewRenderer : CastcleViewRenderer<SettingProfileViewEntity,
    SettingProfileViewHolder,
    SettingListener>(R.layout.item_setting_profile) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingProfileViewHolder(
        ItemSettingProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}