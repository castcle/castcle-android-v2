package com.castcle.android.presentation.setting.item_add_page

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSettingAddPageBinding
import com.castcle.android.presentation.setting.SettingListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SettingAddPageViewHolder(
    binding: ItemSettingAddPageBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SettingListener,
) : CastcleViewHolder<SettingAddPageViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onNewPageClick()
        }
    }

    override var item = SettingAddPageViewEntity()

}