package com.castcle.android.presentation.setting.item_profile_section

import android.view.LayoutInflater
import android.view.ViewGroup
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewRenderer
import com.castcle.android.databinding.ItemSettingProfileSectionBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable

class SettingProfileSectionViewRenderer : CastcleViewRenderer<SettingProfileSectionViewEntity,
    SettingProfileSectionViewHolder,
    SettingListener>(R.layout.item_setting_profile_section) {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: SettingListener,
        compositeDisposable: CompositeDisposable
    ) = SettingProfileSectionViewHolder(
        ItemSettingProfileSectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), compositeDisposable, listener
    )

}